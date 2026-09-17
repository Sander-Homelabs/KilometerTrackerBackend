GRANT INSERT ON kilometer_tracker.users TO api;

CREATE POLICY users_insert_new
    ON kilometer_tracker.users
    FOR INSERT
    WITH CHECK (true);