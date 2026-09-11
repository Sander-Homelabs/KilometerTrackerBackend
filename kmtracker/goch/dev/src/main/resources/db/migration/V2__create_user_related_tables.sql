CREATE TABLE kilometer_tracker.user_status (
    status VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE kilometer_tracker.users (
    email VARCHAR(254) NOT NULL PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    status VARCHAR(50),

    CONSTRAINT fk_status
        FOREIGN KEY (status)
            REFERENCES kilometer_tracker.user_status(status)
);

CREATE TABLE kilometer_tracker.user_password (
    email VARCHAR(254) NOT NULL,
    password TEXT NOT NULL,
    active BOOLEAN NOT NULL,

    PRIMARY KEY (email, password),

    CONSTRAINT fk_email
        FOREIGN KEY (email)
            REFERENCES kilometer_tracker.users(email)
);

CREATE TABLE kilometer_tracker.refresh_token (
    email VARCHAR(254) NOT NULL,
    token TEXT NOT NULL,
    active BOOLEAN NOT NULL,

    PRIMARY KEY (email, token),

    CONSTRAINT fk_email
        FOREIGN KEY (email)
            REFERENCES kilometer_tracker.users(email)
);
