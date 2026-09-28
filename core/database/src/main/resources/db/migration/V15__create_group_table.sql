-- Tables ----------------------------------------------------------------

CREATE TABLE kilometer_tracker.groups (
                                          id         TEXT         NOT NULL PRIMARY KEY,
                                          name       VARCHAR(50)  NOT NULL,
                                          created_by VARCHAR(254) NOT NULL,

                                          CONSTRAINT fk_groups_created_by
                                              FOREIGN KEY (created_by)
                                                  REFERENCES kilometer_tracker.users(email)
);

CREATE TABLE kilometer_tracker.group_role (
                                              role VARCHAR(50) NOT NULL PRIMARY KEY
);

INSERT INTO kilometer_tracker.group_role (role)
VALUES ('MEMBER'), ('ADMIN');

CREATE TABLE kilometer_tracker.group_user (
                                              email    VARCHAR(254) NOT NULL,
                                              group_id TEXT         NOT NULL,
                                              role     VARCHAR(50)  NOT NULL DEFAULT 'MEMBER',

                                              PRIMARY KEY (email, group_id),

                                              CONSTRAINT fk_group_user_email
                                                  FOREIGN KEY (email)
                                                      REFERENCES kilometer_tracker.users(email),

                                              CONSTRAINT fk_group_user_group
                                                  FOREIGN KEY (group_id)
                                                      REFERENCES kilometer_tracker.groups(id),

                                              CONSTRAINT fk_group_user_role
                                                  FOREIGN KEY (role)
                                                      REFERENCES kilometer_tracker.group_role(role)
);

CREATE INDEX idx_group_user_group_id
    ON kilometer_tracker.group_user(group_id);


-- SECURITY DEFINER helpers
-- These execute with the privileges of their superuser owner (postgres superuser),
-- allowing them to inspect RLS-protected tables without policy recursion.


CREATE OR REPLACE FUNCTION kilometer_tracker.is_group_member(p_group_id TEXT)
RETURNS BOOLEAN
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = kilometer_tracker, pg_temp
AS $$
SELECT EXISTS (
    SELECT 1
    FROM kilometer_tracker.group_user
    WHERE group_id = p_group_id
      AND email = kilometer_tracker.current_app_email()
);
$$;

CREATE OR REPLACE FUNCTION kilometer_tracker.is_group_admin(p_group_id TEXT)
RETURNS BOOLEAN
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = kilometer_tracker, pg_temp
AS $$
SELECT EXISTS (
    SELECT 1
    FROM kilometer_tracker.group_user
    WHERE group_id = p_group_id
      AND email = kilometer_tracker.current_app_email()
      AND role = 'ADMIN'
);
$$;

CREATE OR REPLACE FUNCTION kilometer_tracker.group_has_members(p_group_id TEXT)
RETURNS BOOLEAN
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = kilometer_tracker, pg_temp
AS $$
SELECT EXISTS (
    SELECT 1
    FROM kilometer_tracker.group_user
    WHERE group_id = p_group_id
);
$$;

CREATE OR REPLACE FUNCTION kilometer_tracker.is_group_creator(p_group_id TEXT)
RETURNS BOOLEAN
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = kilometer_tracker, pg_temp
AS $$
SELECT EXISTS (
    SELECT 1
    FROM kilometer_tracker.groups
    WHERE id = p_group_id
      AND created_by = kilometer_tracker.current_app_email()
);
$$;


-- groups ------------------------------------------------------------------

ALTER TABLE kilometer_tracker.groups ENABLE ROW LEVEL SECURITY;
ALTER TABLE kilometer_tracker.groups FORCE ROW LEVEL SECURITY;

CREATE POLICY groups_insert_own
    ON kilometer_tracker.groups
    FOR INSERT
    WITH CHECK (
        kilometer_tracker.current_app_email() IS NOT NULL
        AND created_by = kilometer_tracker.current_app_email()
    );

CREATE POLICY groups_select_member
    ON kilometer_tracker.groups
    FOR SELECT
    USING (
        kilometer_tracker.is_group_member(id)
        OR kilometer_tracker.is_group_creator(id)
    );


CREATE POLICY groups_update_name_admin
    ON kilometer_tracker.groups
    FOR UPDATE
        USING (
            kilometer_tracker.is_group_admin(id)
       )
        WITH CHECK (
            kilometer_tracker.is_group_admin(id)
       );

GRANT SELECT, UPDATE(name), INSERT ON kilometer_tracker.groups TO api;


-- group_role --------------------------------------------------------------

ALTER TABLE kilometer_tracker.group_role ENABLE ROW LEVEL SECURITY;
ALTER TABLE kilometer_tracker.group_role FORCE ROW LEVEL SECURITY;

CREATE POLICY group_role_select_all
    ON kilometer_tracker.group_role
    FOR SELECT
                   USING (true);

GRANT SELECT ON kilometer_tracker.group_role TO api;


-- group_user --------------------------------------------------------------

ALTER TABLE kilometer_tracker.group_user ENABLE ROW LEVEL SECURITY;
ALTER TABLE kilometer_tracker.group_user FORCE ROW LEVEL SECURITY;

CREATE POLICY group_user_select_member
    ON kilometer_tracker.group_user
    FOR SELECT
        USING (
            kilometer_tracker.is_group_member(group_id)
            OR kilometer_tracker.is_group_creator(group_id)
       );


CREATE POLICY group_user_insert
    ON kilometer_tracker.group_user
    FOR INSERT
    WITH CHECK (
        kilometer_tracker.is_group_admin(group_id)
        OR (
            email = kilometer_tracker.current_app_email()
            AND role = 'ADMIN'
            AND NOT kilometer_tracker.group_has_members(group_id)
            AND kilometer_tracker.is_group_creator(group_id)
        )
    );


GRANT SELECT, INSERT ON kilometer_tracker.group_user TO api;

REVOKE EXECUTE ON FUNCTION kilometer_tracker.is_group_member(TEXT) FROM PUBLIC;
REVOKE EXECUTE ON FUNCTION kilometer_tracker.is_group_admin(TEXT) FROM PUBLIC;
REVOKE EXECUTE ON FUNCTION kilometer_tracker.group_has_members(TEXT) FROM PUBLIC;
REVOKE EXECUTE ON FUNCTION kilometer_tracker.is_group_creator(TEXT) FROM PUBLIC;

GRANT EXECUTE ON FUNCTION kilometer_tracker.is_group_member(TEXT) TO api;
GRANT EXECUTE ON FUNCTION kilometer_tracker.is_group_admin(TEXT) TO api;
GRANT EXECUTE ON FUNCTION kilometer_tracker.group_has_members(TEXT) TO api;
GRANT EXECUTE ON FUNCTION kilometer_tracker.is_group_creator(TEXT) TO api;
