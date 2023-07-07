ALTER TABLE user_details
ADD CONSTRAINT unique_mobilephone_constraint UNIQUE (mobilePhone),
ADD CONSTRAINT unique_telegramId_constraint UNIQUE (telegramId);