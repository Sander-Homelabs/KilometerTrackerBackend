DROP POLICY groups_select_member
    ON kilometer_tracker.groups;

DROP POLICY groups_update_name_admin
    ON kilometer_tracker.groups;

DROP POLICY group_user_select_member
    ON kilometer_tracker.group_user;

DROP POLICY group_user_insert
    ON kilometer_tracker.group_user;


ALTER TABLE kilometer_tracker.group_user
DROP CONSTRAINT fk_group_user_group;


ALTER TABLE kilometer_tracker.groups
ALTER COLUMN id TYPE UUID
    USING id::uuid;


ALTER TABLE kilometer_tracker.group_user
ALTER COLUMN group_id TYPE UUID
    USING group_id::uuid;


ALTER TABLE kilometer_tracker.group_user
    ADD CONSTRAINT fk_group_user_group
        FOREIGN KEY (group_id)
            REFERENCES kilometer_tracker.groups(id);


DROP FUNCTION kilometer_tracker.is_group_member(TEXT);
DROP FUNCTION kilometer_tracker.is_group_admin(TEXT);
DROP FUNCTION kilometer_tracker.group_has_members(TEXT);
DROP FUNCTION kilometer_tracker.is_group_creator(TEXT);


CREATE FUNCTION kilometer_tracker.is_group_member(p_group_id UUID)
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


CREATE FUNCTION kilometer_tracker.is_group_admin(p_group_id UUID)
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


CREATE FUNCTION kilometer_tracker.group_has_members(p_group_id UUID)
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


CREATE FUNCTION kilometer_tracker.is_group_creator(p_group_id UUID)
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


REVOKE EXECUTE
    ON FUNCTION kilometer_tracker.is_group_member(UUID)
    FROM PUBLIC;

REVOKE EXECUTE
    ON FUNCTION kilometer_tracker.is_group_admin(UUID)
    FROM PUBLIC;

REVOKE EXECUTE
    ON FUNCTION kilometer_tracker.group_has_members(UUID)
    FROM PUBLIC;

REVOKE EXECUTE
    ON FUNCTION kilometer_tracker.is_group_creator(UUID)
    FROM PUBLIC;


GRANT EXECUTE
ON FUNCTION kilometer_tracker.is_group_member(UUID)
    TO api;

GRANT EXECUTE
    ON FUNCTION kilometer_tracker.is_group_admin(UUID)
    TO api;

GRANT EXECUTE
    ON FUNCTION kilometer_tracker.group_has_members(UUID)
    TO api;

GRANT EXECUTE
    ON FUNCTION kilometer_tracker.is_group_creator(UUID)
    TO api;


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
