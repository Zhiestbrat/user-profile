ALTER table user_details
alter column user_id type bigint;

ALTER TABLE user_details
ADD CONSTRAINT fk_user_details_user_id
FOREIGN KEY (user_id)
REFERENCES users (id);