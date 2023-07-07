ALTER TABLE user_details
ADD CONSTRAINT fk_user_details_user_id
FOREIGN KEY (user_id)
REFERENCES users (id) USING index;