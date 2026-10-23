ALTER TABLE kilometer_tracker.trips
    ALTER COLUMN registered_by SET DEFAULT kilometer_tracker.current_app_email();