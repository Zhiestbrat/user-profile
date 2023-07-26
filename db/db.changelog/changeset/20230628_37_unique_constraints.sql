ALTER TABLE user_details
ADD CONSTRAINT unique_mobilephone_constraint UNIQUE (mobile_phone),
ADD CONSTRAINT unique_telegramId_constraint UNIQUE (telegram_id);