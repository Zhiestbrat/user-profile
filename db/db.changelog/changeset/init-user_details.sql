CREATE TABLE user_details
(
    id             BIGSERIAL PRIMARY KEY,
    telegram_id    VARCHAR(255)        NOT NULL,
    mobile_phone   VARCHAR(255)        NOT NULL
);