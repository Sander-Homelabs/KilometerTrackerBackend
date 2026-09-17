-- V7__add_user_role.sql

CREATE TABLE kilometer_tracker.user_role (
    role VARCHAR(50) NOT NULL PRIMARY KEY
);

INSERT INTO kilometer_tracker.user_role (role)
VALUES ('admin'), ('user');

ALTER TABLE kilometer_tracker.users
    ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'user'
        REFERENCES kilometer_tracker.user_role(role);


-- user_role

ALTER TABLE kilometer_tracker.user_role
    ENABLE ROW LEVEL SECURITY;

ALTER TABLE kilometer_tracker.user_role
    FORCE ROW LEVEL SECURITY;

CREATE POLICY user_role_select_all
    ON kilometer_tracker.user_role
    FOR SELECT
                   USING (true);