-- 브랜드 데이터 삽입
INSERT INTO brand (name, image, description) VALUES ('Brand A', 'imageA.jpg', 'Description A');
SET @brand_id = LAST_INSERT_ID();
INSERT INTO partners (email, password, business_number, brand_id) VALUES ('partner1@example.com', 'password1', '1234567890', @brand_id);

-- 파트너 데이터 삽입
INSERT INTO brand (name, image, description) VALUES ('Brand B', 'imageB.jpg', 'Description B');
SET @brand_id = LAST_INSERT_ID();
INSERT INTO partners (email, password, business_number, brand_id) VALUES ('partner2@example.com', 'password2', '0987654321', @brand_id);

-- 아이템 데이터 삽입
INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item 1', 1000, 'image1.jpg', 'Description 1', 1, NOW(), NOW());
INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item 2', 2000, 'image2.jpg', 'Description 2', 1, NOW(), NOW());
INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item 3', 3000, 'image3.jpg', 'Description 3', 2, NOW(), NOW());

-- 아이템 옵션 데이터 삽입
INSERT INTO item_option (name, quantity, option_price, item_id)
VALUES ('Option 1', 10, 100, 1);
INSERT INTO item_option (name, quantity, option_price, item_id)
VALUES ('Option 2', 5, 200, 1);
INSERT INTO item_option (name, quantity, option_price, item_id)
VALUES ('Option 3', 20, 300, 2);