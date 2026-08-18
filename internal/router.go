package internal

import (
	"net/http"

	"github.com/jackc/pgx/v5/pgxpool"
	"goch.dev/kmtracker/internal/user"
)

func NewRouter(db *pgxpool.Pool) *http.ServeMux {
	mux := http.NewServeMux()

	mux.Handle("/user/", user.NewRouter(db))

	return mux
}