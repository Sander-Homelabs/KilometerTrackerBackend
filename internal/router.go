package internal

import (
	"net/http"

	"goch.dev/kmtracker/internal/auth"
	"goch.dev/kmtracker/internal/db"
	"goch.dev/kmtracker/internal/user"
)

func NewRouter(db *db.DB, jwtService *auth.JwtService) *http.ServeMux {
	mux := http.NewServeMux()

	mux.Handle("/user/", user.NewRouter(db))

	return mux
}