package db

import (
	"context"

	"github.com/jackc/pgx/v5/pgxpool"
)

type DB struct {
	Pool *pgxpool.Pool
}

func NewDB(pool *pgxpool.Pool) *DB {
	return &DB{
		Pool: pool,
	}
}

func (d *DB) Queries() *Queries {
	return New(d.Pool)
}

func (d *DB) WithUser(
	ctx context.Context,
	email string,
	fn func(*Queries) error,
) error {
	tx, err := d.Pool.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	_, err = tx.Exec(
		ctx,
		`SELECT set_config('app.current_email', $1, true)`,
		email,
	)
	if err != nil {
		return err
	}

	q := New(tx)

	if err := fn(q); err != nil {
		return err
	}

	return tx.Commit(ctx)
}
