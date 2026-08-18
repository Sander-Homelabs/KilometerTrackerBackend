package user

import (
	"net/http"

	"github.com/jackc/pgx/v5/pgxpool"
)

func NewRouter(db *pgxpool.Pool) *http.ServeMux {
	mux := http.NewServeMux()

	repository := NewRepository(db)
	business := NewBusiness(repository)
	handler := NewHandler(business)

	mux.HandleFunc("GET /", handler.RegisterUser)

	return mux
}