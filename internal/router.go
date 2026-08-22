package internal

import (
	"net/http"

	"github.com/jackc/pgx/v5/pgxpool"
	"goch.dev/kmtracker/internal/user"
	"goch.dev/kmtracker/internal/auth"
)

func NewRouter(db *pgxpool.Pool, jwtService *auth.JwtService) *http.ServeMux {
	mux := http.NewServeMux()

	mux.Handle("/user/", user.NewRouter(db))

	return mux
}