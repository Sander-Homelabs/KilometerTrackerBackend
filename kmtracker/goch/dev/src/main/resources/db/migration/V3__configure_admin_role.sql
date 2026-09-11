-- V3__create_api_admin_role.sql

DO $$
BEGIN
IF NOT EXISTS (
   SELECT 1
   FROM pg_roles
   WHERE rolname = 'api_admin'
   ) THEN
CREATE ROLE api_admin NOLOGIN BYPASSRLS;
END IF;
END
$$;

GRANT api_admin TO api;

ALTER ROLE api NOINHERIT;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON ALL TABLES IN SCHEMA kilometer_tracker
        TO api_admin;

GRANT USAGE, SELECT
    ON ALL SEQUENCES IN SCHEMA kilometer_tracker
        TO api_admin;
