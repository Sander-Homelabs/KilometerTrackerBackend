ALTER TABLE kilometer_tracker.user_confirmation_code
    ADD COLUMN createdAt TIMESTAMPTZ NOT NULL DEFAULT now();