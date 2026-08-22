package auth

import (
	"crypto/ecdsa"
	"crypto/x509"
	"encoding/pem"
	"fmt"
	"os"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

const (
	AccessTokenExpiry  = 15 * time.Minute
	RefreshTokenExpiry = 14 * 24 * time.Hour
)

type JwtService struct {
	db *pgxpool.Pool
	accessPrivateKey *ecdsa.PrivateKey
	refreshPrivateKey *ecdsa.PrivateKey
}

type JwtRefreshResponse struct {
	AccessToken  string
	RefreshToken string
}

func NewJwtService(db *pgxpool.Pool) (*JwtService, error) {
	accessKeyString := os.Getenv("ACCESS_TOKEN_KEY")
	if accessKeyString == "" {
		return nil, fmt.Errorf("ACCESS_TOKEN_KEY is not set")
	}

	accessKeyBlock, _ := pem.Decode([]byte(accessKeyString))
	if accessKeyBlock == nil {
		return nil, fmt.Errorf("failed to decode PEM block for access token key")
	}

	accessKey, err := x509.ParseECPrivateKey(accessKeyBlock.Bytes)
	if err != nil {
		return nil, fmt.Errorf("failed to parse ECDSA private key: %w", err)
	}

	refreshKeyString := os.Getenv("REFRESH_TOKEN_KEY")
	if refreshKeyString == "" {
		return nil, fmt.Errorf("REFRESH_TOKEN_KEY is not set")
	}

	refreshKeyBlock, _ := pem.Decode([]byte(refreshKeyString))
	if refreshKeyBlock == nil {
		return nil, fmt.Errorf("failed to decode PEM block for refresh token key")
	}

	refreshKey, err := x509.ParseECPrivateKey(refreshKeyBlock.Bytes)
	if err != nil {
		return nil, fmt.Errorf("failed to parse ECDSA private key: %w", err)
	}

	return &JwtService{db: db, accessPrivateKey: accessKey, refreshPrivateKey: refreshKey}, nil
}

func (jwtService JwtService) SignAccessToken(email string) (string, error) {
	now := time.Now()
	claims := jwt.MapClaims{
		"email": email,
		"iat":   now.Unix(),
		"exp":   now.Add(AccessTokenExpiry).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodES512, claims)

	signed, err := token.SignedString(jwtService.accessPrivateKey)
	if err != nil {
		return "", fmt.Errorf("failed to sign access token: %w", err)
	}

	return signed, nil
}

func (jwtService JwtService) SignRefreshToken(email string) (string, error) {
	now := time.Now()
	claims := jwt.MapClaims{
		"email": email,
		"iat":   now.Unix(),
		"exp":   now.Add(RefreshTokenExpiry).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodES512, claims)

	signed, err := token.SignedString(jwtService.refreshPrivateKey)
	if err != nil {
		return "", fmt.Errorf("failed to sign access token: %w", err)
	}

	return signed, nil
}

func (jwtService JwtService) VerifyAccessToken(tokenString string) (string, error) {
    token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
        if token.Method != jwt.SigningMethodES512 {
            return nil, fmt.Errorf("unexpected signing method: %v", token.Method.Alg())
        }

        return &jwtService.accessPrivateKey.PublicKey, nil
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
        return "", fmt.Errorf("refresh token has no valid subject")
    }

    return email, nil
}

func (jwtService JwtService) VerifyRefreshToken(tokenString string) (string, error) {
    token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
        if token.Method != jwt.SigningMethodES512 {
            return nil, fmt.Errorf("unexpected signing method: %v", token.Method.Alg())
        }

        return &jwtService.refreshPrivateKey.PublicKey, nil
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
        return "", fmt.Errorf("refresh token has no valid subject")
    }

    return email, nil
}

func (jwtService JwtService) RefreshTokens(
    refreshToken string,
) (JwtRefreshResponse, error) {
    email, err := jwtService.VerifyRefreshToken(refreshToken)
    if err != nil {
        return JwtRefreshResponse{}, err
    }

    accessToken, err := jwtService.SignAccessToken(email)
    if err != nil {
        return JwtRefreshResponse{}, fmt.Errorf(
            "failed to create access token: %w",
            err,
        )
    }

    newRefreshToken, err := jwtService.SignRefreshToken(email)
    if err != nil {
        return JwtRefreshResponse{}, fmt.Errorf(
            "failed to create refresh token: %w",
            err,
        )
    }

    return JwtRefreshResponse{
        AccessToken:  accessToken,
        RefreshToken: newRefreshToken,
    }, nil
}
