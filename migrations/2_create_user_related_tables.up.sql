CREATE TABLE user_status (
    status VARCHAR(50) NOT NULL PRIMARY KEY
);

CREATE TABLE users (
    email VARCHAR(254) NOT NULL PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    status VARCHAR(50),
    
    CONSTRAINT fk_status
    FOREIGN KEY(status)
    REFERENCES user_status(status)
);

CREATE TABLE user_password (
    email VARCHAR(254) NOT NULL,
    password VARCHAR(50) NOT NULL,
    active BOOL NOT NULL,

    PRIMARY KEY(email, password),

    CONSTRAINT fk_email
    FOREIGN KEY(email)
    REFERENCES users(email)
);

CREATE TABLE refresh_token (
    email VARCHAR(254) NOT NULL,
    token TEXT NOT NULL,
    active BOOL NOT NULL,

    PRIMARY KEY(email, token),

    CONSTRAINT fk_email
    FOREIGN KEY(email)
    REFERENCES users(email)
);