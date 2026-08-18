package main

import (
	"context"
	"log"
	"net/http"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/joho/godotenv"
	"goch.dev/kmtracker/internal"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}

	db, err := pgxpool.New(context.Background(), "")
	if (err != nil) {
		log.Fatal(err)
	}
	defer db.Close()

	router := internal.Cors(internal.NewRouter(db))

	server := &http.Server{
		Addr: ":8080",
		Handler: router,
	}

	server.ListenAndServe()
}