ALTER TABLE kilometer_tracker.users
    ALTER COLUMN status SET DEFAULT 'AWAITING_CONFIRMATION';

ALTER TABLE kilometer_tracker.users
    ALTER COLUMN status SET NOT NULL;