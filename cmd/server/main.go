package main

import (
	"context"
	"errors"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/joho/godotenv"
	"goch.dev/kmtracker/internal"
	"goch.dev/kmtracker/internal/auth"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}

	ctx := context.Background()

	db, err := newDBPool(ctx)
	if err != nil {
		log.Fatalf("could not initialize db pool: %v", err)
	}
	defer db.Close()

	jwtService, err := auth.NewJwtService(db)
	if err != nil {
		log.Fatalf("could not initialize jwt service: %v", err)
	}

	router := internal.Cors(internal.NewRouter(db, jwtService))

	server := &http.Server{
		Addr: ":8080",
		Handler: withTimeout(router, 5*time.Second),
	}

	go func() {
		log.Println("listening on :8080")
		if err := server.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			log.Fatalf("server error: %v", err)
		}
	}()

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)
	<-stop

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		log.Printf("graceful shutdown failed: %v", err)
	}
}

func withTimeout(next http.Handler, timeout time.Duration) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), timeout)
		defer cancel()
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func newDBPool(ctx context.Context) (*pgxpool.Pool, error) {
	config, err := pgxpool.ParseConfig(os.Getenv("DATABASE_URL"))
	if err != nil {
		return nil, err
	}

	config.MaxConns = 20
	config.MinConns = 2
	config.MaxConnLifetime = 30 * time.Minute
	config.MaxConnIdleTime = 5 * time.Minute
	config.HealthCheckPeriod = 30 * time.Second

	var pool *pgxpool.Pool
	var lastErr error

	for i := range 5 {
		pool, lastErr = pgxpool.NewWithConfig(ctx, config)
		if lastErr == nil {
			pingCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
			lastErr = pool.Ping(pingCtx)
			cancel()
			if lastErr == nil {
				return pool, nil
			}
		}
		log.Printf("db not ready, retrying in 2m (attempt %d/5): %v", i+1, lastErr)
		time.Sleep(2 * time.Minute)
	}

	return nil, lastErr
}