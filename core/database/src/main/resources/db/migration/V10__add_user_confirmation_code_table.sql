CREATE TABLE kilometer_tracker.user_confirmation_code (
    email VARCHAR(254) NOT NULL PRIMARY KEY,
    code  UUID         NOT NULL,

    CONSTRAINT fk_user_confirmation_code_email
        FOREIGN KEY (email)
            REFERENCES kilometer_tracker.users(email)
);

ALTER TABLE kilometer_tracker.user_confirmation_code
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.user_confirmation_code
    FORCE ROW LEVEL SECURITY;

CREATE POLICY user_confirmation_code_insert_all
    ON kilometer_tracker.user_confirmation_code
    FOR INSERT
    WITH CHECK (true);

CREATE POLICY user_confirmation_code_select_all
    ON kilometer_tracker.user_confirmation_code
    FOR SELECT
                          USING (true);

CREATE POLICY user_confirmation_code_delete_all
    ON kilometer_tracker.user_confirmation_code
    FOR DELETE
USING (true);

GRANT INSERT, SELECT, DELETE ON kilometer_tracker.user_confirmation_code TO api;