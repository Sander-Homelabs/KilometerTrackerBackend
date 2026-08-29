package auth

import (
	"context"

	"goch.dev/kmtracker/internal/db"
)

type Repository struct {
	db *db.DB
}

func NewRepository(database *db.DB) *Repository {
	return &Repository{db: database}
}

func (r *Repository) InsertRefreshToken(
	ctx context.Context,
	email string,
	token string,
) error {
	return r.db.WithUser(ctx, email, func(q *db.Queries) error {
		return q.InsertRefreshToken(ctx, db.InsertRefreshTokenParams{
			Email: email,
			Token: token,
		})
	})
}

func (r *Repository) DisableRefreshToken(
	ctx context.Context,
	email string,
	token string,
) error {
	return r.db.WithUser(ctx, email, func(q *db.Queries) error {
		return q.DisableRefreshToken(ctx, token)
	})
}

func (r *Repository) GetRefreshToken(
	ctx context.Context,
	email string,
	token string,
) (db.RefreshToken, error) {
	var result db.RefreshToken

	err := r.db.WithUser(ctx, email, func(q *db.Queries) error {
		var err error

		result, err = q.GetRefreshToken(ctx, token)

		return err
	})

	return result, err
}
