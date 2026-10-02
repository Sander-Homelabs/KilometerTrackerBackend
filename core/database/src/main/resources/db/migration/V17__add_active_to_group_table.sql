ALTER TABLE kilometer_tracker.groups
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;

DROP POLICY groups_select_member
    ON kilometer_tracker.groups;

CREATE POLICY groups_select_member
    ON kilometer_tracker.groups
    FOR SELECT
                        USING (
                        active = true
                        AND (
                        kilometer_tracker.is_group_member(id)
                        OR kilometer_tracker.is_group_creator(id)
                        )
                        );

DROP POLICY groups_update_name_admin
    ON kilometer_tracker.groups;

CREATE POLICY groups_update_name_admin
    ON kilometer_tracker.groups
    FOR UPDATE
                        USING (
                        active = true
                        AND kilometer_tracker.is_group_admin(id)
                        )
        WITH CHECK (
                        kilometer_tracker.is_group_admin(id)
                        );

DROP POLICY group_user_select_member
    ON kilometer_tracker.group_user;

CREATE POLICY group_user_select_member
    ON kilometer_tracker.group_user
    FOR SELECT
                        USING (
                        EXISTS (
                        SELECT 1
                        FROM kilometer_tracker.groups g
                        WHERE g.id = group_user.group_id
                        AND g.active = true
                        )
                        AND (
                        kilometer_tracker.is_group_member(group_id)
                        OR kilometer_tracker.is_group_creator(group_id)
                        )
                        );

DROP POLICY group_user_insert
    ON kilometer_tracker.group_user;

CREATE POLICY group_user_insert
    ON kilometer_tracker.group_user
    FOR INSERT
    WITH CHECK (
        EXISTS (
            SELECT 1
            FROM kilometer_tracker.groups g
            WHERE g.id = group_user.group_id
              AND g.active = true
        )
        AND (
            kilometer_tracker.is_group_admin(group_id)
            OR (
                email = kilometer_tracker.current_app_email()
                AND role = 'ADMIN'
                AND NOT kilometer_tracker.group_has_members(group_id)
                AND kilometer_tracker.is_group_creator(group_id)
            )
        )
    );
