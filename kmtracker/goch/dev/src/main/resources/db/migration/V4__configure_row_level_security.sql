-- V4__configure_row_level_security.sql

CREATE OR REPLACE FUNCTION kilometer_tracker.current_app_email()
RETURNS VARCHAR(254)
LANGUAGE sql
STABLE
AS $$
SELECT NULLIF(
               current_setting('app.current_email', true),
               ''
       )::VARCHAR(254);
$$;


-- user_status

ALTER TABLE kilometer_tracker.user_status
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.user_status
    FORCE ROW LEVEL SECURITY;

CREATE POLICY user_status_select_all
    ON kilometer_tracker.user_status
    FOR SELECT
                   USING (true);


-- users

ALTER TABLE kilometer_tracker.users
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.users
    FORCE ROW LEVEL SECURITY;

CREATE POLICY users_select_own
    ON kilometer_tracker.users
    FOR SELECT
                   USING (
                   email = kilometer_tracker.current_app_email()
                   );

GRANT UPDATE (firstName, lastName)
    ON kilometer_tracker.users
    TO api;


-- user_password

ALTER TABLE kilometer_tracker.user_password
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.user_password
    FORCE ROW LEVEL SECURITY;

CREATE POLICY user_password_select_own
    ON kilometer_tracker.user_password
    FOR SELECT
                   USING (
                   email = kilometer_tracker.current_app_email()
                   );

CREATE POLICY user_password_insert_own
    ON kilometer_tracker.user_password
    FOR INSERT
    WITH CHECK (
        email = kilometer_tracker.current_app_email()
    );

CREATE POLICY user_password_update_own
    ON kilometer_tracker.user_password
    FOR UPDATE
                          USING (
                          email = kilometer_tracker.current_app_email()
                          )
        WITH CHECK (
                          email = kilometer_tracker.current_app_email()
                          );


-- refresh_token

ALTER TABLE kilometer_tracker.refresh_token
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.refresh_token
    FORCE ROW LEVEL SECURITY;

CREATE POLICY refresh_token_select_own
    ON kilometer_tracker.refresh_token
    FOR SELECT
                   USING (
                   email = kilometer_tracker.current_app_email()
                   );

CREATE POLICY refresh_token_insert_own
    ON kilometer_tracker.refresh_token
    FOR INSERT
    WITH CHECK (
        email = kilometer_tracker.current_app_email()
    );

CREATE POLICY refresh_token_update_own
    ON kilometer_tracker.refresh_token
    FOR UPDATE
                          USING (
                          email = kilometer_tracker.current_app_email()
                          )
        WITH CHECK (
                          email = kilometer_tracker.current_app_email()
                          );
