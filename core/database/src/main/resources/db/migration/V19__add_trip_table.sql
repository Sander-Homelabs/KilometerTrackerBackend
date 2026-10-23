CREATE OR REPLACE FUNCTION kilometer_tracker.is_user_in_group(
       p_group_id UUID,
       p_email VARCHAR
)
       RETURNS BOOLEAN
       LANGUAGE sql
       STABLE
       SECURITY DEFINER
       SET search_path = kilometer_tracker, pg_temp
       AS $$
            SELECT EXISTS(
                SELECT 1
                FROM kilometer_tracker.group_user gu
                WHERE gu.group_id = p_group_id
                    AND gu.email = p_email
            );
       $$;

REVOKE EXECUTE
    ON FUNCTION kilometer_tracker.is_user_in_group(UUID, VARCHAR)
    FROM PUBLIC;

GRANT EXECUTE
    ON FUNCTION kilometer_tracker.is_user_in_group(UUID, VARCHAR)
    TO api;

CREATE TABLE kilometer_tracker.trips (
                                         group_id         UUID         NOT NULL,
                                         start       INT NOT NULL,
                                         "end"         INT NOT NULL,
                                         email       VARCHAR(254)  NOT NULL,
                                         registered_by VARCHAR(254) NOT NULL,
                                         created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                                         PRIMARY KEY (group_id, start),

                                         CONSTRAINT fk_groups_id
                                             FOREIGN KEY (group_id)
                                                 REFERENCES kilometer_tracker.groups(id),

                                         CONSTRAINT fk_users_email
                                             FOREIGN KEY (email)
                                                 REFERENCES kilometer_tracker.users(email),

                                         CONSTRAINT fk_users_email_registered_by
                                             FOREIGN KEY (registered_by)
                                                 REFERENCES kilometer_tracker.users(email)
);

CREATE INDEX idx_trips_group_id
    ON kilometer_tracker.trips(group_id);

ALTER TABLE kilometer_tracker.trips ENABLE ROW LEVEL SECURITY;
ALTER TABLE kilometer_tracker.trips FORCE ROW LEVEL SECURITY;

CREATE POLICY trips_select
    ON kilometer_tracker.trips
    FOR SELECT
                   USING (
                   kilometer_tracker.is_group_member(group_id)
                   );

CREATE POLICY trips_insert
    ON kilometer_tracker.trips
    FOR INSERT
                   WITH CHECK (
                   kilometer_tracker.is_group_member(group_id)
                   AND registered_by = kilometer_tracker.current_app_email()
                   AND kilometer_tracker.is_user_in_group(group_id, email)
                   );

CREATE POLICY trips_delete
    ON kilometer_tracker.trips
    FOR DELETE
                   USING (
                   kilometer_tracker.is_group_member(group_id)
                   );
