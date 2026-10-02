DROP POLICY groups_select_member
    ON kilometer_tracker.groups;

CREATE POLICY groups_select_member
    ON kilometer_tracker.groups
    FOR SELECT
                        USING (
                        active = true
                        AND (
                        created_by = kilometer_tracker.current_app_email()
                        OR kilometer_tracker.is_group_member(id)
                        )
                        );