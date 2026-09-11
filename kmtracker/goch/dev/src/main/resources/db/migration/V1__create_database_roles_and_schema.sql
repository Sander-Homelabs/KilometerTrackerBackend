-- V1__create_api_role_and_schema.sql

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_roles
        WHERE rolname = 'api'
    ) THEN
CREATE ROLE api LOGIN PASSWORD 'postgres';
END IF;
END
$$;

CREATE SCHEMA IF NOT EXISTS kilometer_tracker;

GRANT CONNECT ON DATABASE postgres TO api;
GRANT USAGE ON SCHEMA kilometer_tracker TO api;
