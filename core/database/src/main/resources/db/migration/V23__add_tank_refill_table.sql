CREATE TABLE kilometer_tracker.tank_refill(
    group_id UUID NOT NULL,
    start INT NOT NULL,
    "end" INT NOT NULL,
    cost NUMERIC NOT NULL,
    tanker VARCHAR(254) NOT NULL DEFAULT kilometer_tracker.current_app_email(),

    PRIMARY KEY (group_id, start),

    CONSTRAINT fk_groups_id
        FOREIGN KEY (group_id)
            REFERENCES kilometer_tracker.groups(id),

    CONSTRAINT fk_users_email
        FOREIGN KEY (tanker)
            REFERENCES kilometer_tracker.users(email)
);

ALTER TABLE kilometer_tracker.tank_refill ENABLE ROW LEVEL SECURITY;
ALTER TABLE kilometer_tracker.tank_refill FORCE ROW LEVEL SECURITY;

CREATE POLICY tank_refill_select
    ON kilometer_tracker.tank_refill
    FOR SELECT
        USING (kilometer_tracker.is_group_member(group_id));

CREATE POLICY tank_refill_insert
    ON kilometer_tracker.tank_refill
    FOR INSERT
        WITH CHECK (
            kilometer_tracker.is_group_member(group_id)
            AND kilometer_tracker.is_user_in_group(group_id, tanker)
        );

GRANT SELECT, INSERT ON kilometer_tracker.tank_refill TO api;
