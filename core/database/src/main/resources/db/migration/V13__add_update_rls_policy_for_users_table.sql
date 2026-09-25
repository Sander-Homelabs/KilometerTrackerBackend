CREATE POLICY users_update_own
    ON kilometer_tracker.users
    FOR UPDATE
                   USING (
                   email = kilometer_tracker.current_app_email()
                   )
        WITH CHECK (
                   email = kilometer_tracker.current_app_email()
                   );
