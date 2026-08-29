package auth

import (
    "context"
	"goch.dev/kmtracker/internal/db"
)

type Repository struct {
    db *db.Queries
}

func NewRepository(db *db.Queries) *Repository {
    return &Repository{db: db}
}

func (r *Repository) InsertRefreshToken(ctx context.Context, email string, token string) error {
    return r.db.InsertRefreshToken(ctx, db.InsertRefreshTokenParams{
        Email: email,
        Token: token,
    })
}

func (r *Repository) DisableRefreshToken(ctx context.Context, token string) error {
    return r.db.DisableRefreshToken(ctx, token)
}

func (r *Repository) GetRefreshToken(ctx context.Context, token string) (db.RefreshToken, error) {
    return r.db.GetRefreshToken(ctx, token)
}
