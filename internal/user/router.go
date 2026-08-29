package user

import (
	"net/http"

	"goch.dev/kmtracker/internal/db"
)

func NewRouter(db *db.Queries) *http.ServeMux {
	mux := http.NewServeMux()

	repository := NewRepository(db)
	business := NewBusiness(repository)
	handler := NewHandler(business)

	mux.HandleFunc("GET /", handler.RegisterUser)

	return mux
}