-- name: GetRefreshToken :one
SELECT email, token, active
FROM refresh_token
WHERE token = $1;

-- name: InsertRefreshToken :exec
INSERT INTO refresh_token (email, token, active)
VALUES ($1, $2, true);

-- name: DisableRefreshToken :exec
UPDATE refresh_token
SET active = false
WHERE token = $1;
