package user

import (
	"goch.dev/kmtracker/internal/db"
)

type Repository struct {
    db *db.DB
}

func NewRepository(db *db.DB) *Repository {
    return &Repository{db: db}
}
