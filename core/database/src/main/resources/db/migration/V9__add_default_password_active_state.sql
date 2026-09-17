ALTER TABLE kilometer_tracker.user_password
    ALTER COLUMN active SET DEFAULT true;

CREATE UNIQUE INDEX user_password_one_active_per_email
    ON kilometer_tracker.user_password (email)
    WHERE active = true;