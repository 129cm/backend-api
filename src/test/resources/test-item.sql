INSERT INTO brand (name, image, description)
VALUES ('Brand', 'image_url', 'description');

SET @brand_id = LAST_INSERT_ID();

INSERT INTO partners (email, password, business_number, brand_id)
VALUES ('example@example.com', 'encryptedPassword', '123-45-67890', @brand_id);
