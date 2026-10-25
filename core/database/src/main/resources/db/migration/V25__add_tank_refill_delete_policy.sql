CREATE POLICY tank_refill_delete
    ON kilometer_tracker.tank_refill
    FOR DELETE
USING (
kilometer_tracker.is_group_member(group_id)
);

GRANT DELETE ON kilometer_tracker.tank_refill TO api;
