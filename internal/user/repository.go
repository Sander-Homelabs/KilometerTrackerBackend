package user

import (
	"goch.dev/kmtracker/internal/db"
)

type Repository struct {
    db *db.Queries
}

func NewRepository(db *db.Queries) *Repository {
    return &Repository{db: db}
}
