package auth

import (
	"context"
	"crypto/ecdsa"
	"crypto/x509"
	"encoding/pem"
	"fmt"
	"os"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

const (
	AccessTokenExpiry  = 15 * time.Minute
	RefreshTokenExpiry = 14 * 24 * time.Hour
)

type JwtService struct {
	repository       *Repository
	accessPrivateKey *ecdsa.PrivateKey
	refreshPrivateKey *ecdsa.PrivateKey
}

type JwtRefreshResponse struct {
	AccessToken  string
	RefreshToken string
}

func NewJwtService(repository *Repository) (*JwtService, error) {
	accessPrivateKey, err := loadPrivateKey("ACCESS_TOKEN_KEY")
	if err != nil {
		return nil, err
	}

	refreshPrivateKey, err := loadPrivateKey("REFRESH_TOKEN_KEY")
	if err != nil {
		return nil, err
	}

	return &JwtService{
		repository:        repository,
		accessPrivateKey:  accessPrivateKey,
		refreshPrivateKey: refreshPrivateKey,
	}, nil
}

func loadPrivateKey(envName string) (*ecdsa.PrivateKey, error) {
	keyString := os.Getenv(envName)
	if keyString == "" {
		return nil, fmt.Errorf("%s is not set", envName)
	}

	keyBlock, _ := pem.Decode([]byte(keyString))
	if keyBlock == nil {
		return nil, fmt.Errorf("failed to decode PEM block for %s", envName)
	}

	key, err := x509.ParseECPrivateKey(keyBlock.Bytes)
	if err != nil {
		return nil, fmt.Errorf("failed to parse ECDSA private key %s: %w", envName, err)
	}

	return key, nil
}

func (s *JwtService) SignAccessToken(email string) (string, error) {
	now := time.Now()

	claims := jwt.MapClaims{
		"email": email,
		"iat":   now.Unix(),
		"exp":   now.Add(AccessTokenExpiry).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodES512, claims)

	signed, err := token.SignedString(s.accessPrivateKey)
	if err != nil {
		return "", fmt.Errorf("failed to sign access token: %w", err)
	}

	return signed, nil
}

func (s *JwtService) SignRefreshToken(email string) (string, error) {
	now := time.Now()

	claims := jwt.MapClaims{
		"email": email,
		"iat":   now.Unix(),
		"exp":   now.Add(RefreshTokenExpiry).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodES512, claims)

	signed, err := token.SignedString(s.refreshPrivateKey)
	if err != nil {
		return "", fmt.Errorf("failed to sign refresh token: %w", err)
	}

	return signed, nil
}

func (s *JwtService) VerifyAccessToken(
	ctx context.Context,
	tokenString string,
) (string, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
		if token.Method != jwt.SigningMethodES512 {
			return nil, fmt.Errorf(
				"unexpected signing method: %v",
				token.Method.Alg(),
			)
		}

		return &s.accessPrivateKey.PublicKey, nil
	})

	if err != nil {
		return "", fmt.Errorf("invalid access token: %w", err)
	}

	if !token.Valid {
		return "", fmt.Errorf("invalid access token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return "", fmt.Errorf("invalid token claims")
	}

	email, ok := claims["email"].(string)
	if !ok || email == "" {
		return "", fmt.Errorf("access token has no valid email")
	}

	return email, nil
}

func (s *JwtService) VerifyRefreshToken(
	ctx context.Context,
	tokenString string,
) (string, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
		if token.Method != jwt.SigningMethodES512 {
			return nil, fmt.Errorf(
				"unexpected signing method: %v",
				token.Method.Alg(),
			)
		}

		return &s.refreshPrivateKey.PublicKey, nil
	})

	if err != nil {
		return "", fmt.Errorf("invalid refresh token: %w", err)
	}

	if !token.Valid {
		return "", fmt.Errorf("invalid refresh token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return "", fmt.Errorf("invalid token claims")
	}

	email, ok := claims["email"].(string)
	if !ok || email == "" {
		return "", fmt.Errorf("refresh token has no valid email")
	}
	
	dbToken, err := s.repository.GetRefreshToken(ctx, email, tokenString)
	if err != nil {
		return "", fmt.Errorf("refresh token not found: %w", err)
	}

	if !dbToken.Active {
		return "", fmt.Errorf("refresh token is disabled")
	}

	return email, nil
}

func (s *JwtService) RefreshTokens(
	ctx context.Context,
	refreshToken string,
) (JwtRefreshResponse, error) {
	email, err := s.VerifyRefreshToken(ctx, refreshToken)
	if err != nil {
		return JwtRefreshResponse{}, err
	}

	accessToken, err := s.SignAccessToken(email)
	if err != nil {
		return JwtRefreshResponse{}, fmt.Errorf(
			"failed to create access token: %w",
			err,
		)
	}

	newRefreshToken, err := s.SignRefreshToken(email)
	if err != nil {
		return JwtRefreshResponse{}, fmt.Errorf(
			"failed to create refresh token: %w",
			err,
		)
	}

	if err := s.repository.InsertRefreshToken(
		ctx,
		email,
		newRefreshToken,
	); err != nil {
		return JwtRefreshResponse{}, fmt.Errorf(
			"failed to register refresh token: %w",
			err,
		)
	}

	if err := s.repository.DisableRefreshToken(
		ctx,
		email,
		refreshToken,
	); err != nil {
		_ = s.repository.DisableRefreshToken(ctx, email, newRefreshToken)

		return JwtRefreshResponse{}, fmt.Errorf(
			"failed to disable old refresh token: %w",
			err,
		)
	}

	return JwtRefreshResponse{
		AccessToken:  accessToken,
		RefreshToken: newRefreshToken,
	}, nil
}
