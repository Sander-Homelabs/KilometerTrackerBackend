-- V5__insert_user_statuses.sql

INSERT INTO user_status (status)
VALUES
    ('AWAITING_CONFIRMATION'),
    ('ACTIVE'),
    ('INACTIVE')
    ON CONFLICT (status) DO NOTHING;
