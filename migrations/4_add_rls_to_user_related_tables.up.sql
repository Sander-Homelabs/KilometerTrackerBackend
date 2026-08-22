CREATE OR REPLACE FUNCTION current_app_email()
RETURNS VARCHAR(254)
LANGUAGE sql
STABLE
AS $$
    SELECT NULLIF(current_setting('app.current_email', true), '')::VARCHAR(254);
$$;

ALTER TABLE user_status ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_status FORCE ROW LEVEL SECURITY;
 
CREATE POLICY user_status_select_all
    ON user_status
    FOR SELECT
    USING (true);

ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE users FORCE ROW LEVEL SECURITY;

CREATE POLICY users_select_own
    ON users
    FOR SELECT
    USING (email = current_app_email());

GRANT UPDATE (firstName, lastName) ON users TO api;

ALTER TABLE user_password ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_password FORCE ROW LEVEL SECURITY;

CREATE POLICY user_password_select_own
    ON user_password
    FOR SELECT
    USING (email = current_app_email());

CREATE POLICY user_password_insert_own
    ON user_password
    FOR INSERT
    WITH CHECK (email = current_app_email());
 
CREATE POLICY user_password_update_own
    ON user_password
    FOR UPDATE
    USING (email = current_app_email())
    WITH CHECK (email = current_app_email());

ALTER TABLE refresh_token ENABLE ROW LEVEL SECURITY;
ALTER TABLE refresh_token FORCE ROW LEVEL SECURITY;
 
CREATE POLICY refresh_token_select_own
    ON refresh_token
    FOR SELECT
    USING (email = current_app_email());
 
CREATE POLICY refresh_token_insert_own
    ON refresh_token
    FOR INSERT
    WITH CHECK (email = current_app_email());
 
CREATE POLICY refresh_token_update_own
    ON refresh_token
    FOR UPDATE
    USING (email = current_app_email())
    WITH CHECK (email = current_app_email());
