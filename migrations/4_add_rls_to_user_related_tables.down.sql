DROP POLICY IF EXISTS refresh_token_update_own ON refresh_token;
DROP POLICY IF EXISTS refresh_token_insert_own ON refresh_token;
DROP POLICY IF EXISTS refresh_token_select_own ON refresh_token;

DROP POLICY IF EXISTS user_password_update_own ON user_password;
DROP POLICY IF EXISTS user_password_insert_own ON user_password;
DROP POLICY IF EXISTS user_password_select_own ON user_password;

REVOKE UPDATE (firstName, lastName) ON users FROM api;
DROP POLICY IF EXISTS users_select_own ON users;

DROP POLICY IF EXISTS user_status_select_all ON user_status;

ALTER TABLE refresh_token NO FORCE ROW LEVEL SECURITY;
ALTER TABLE refresh_token DISABLE ROW LEVEL SECURITY;

ALTER TABLE user_password NO FORCE ROW LEVEL SECURITY;
ALTER TABLE user_password DISABLE ROW LEVEL SECURITY;

ALTER TABLE users NO FORCE ROW LEVEL SECURITY;
ALTER TABLE users DISABLE ROW LEVEL SECURITY;

ALTER TABLE user_status NO FORCE ROW LEVEL SECURITY;
ALTER TABLE user_status DISABLE ROW LEVEL SECURITY;

DROP FUNCTION IF EXISTS current_app_email();