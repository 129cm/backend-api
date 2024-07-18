INSERT INTO brand (name, image, description)
VALUES ('Brand', 'image_url', 'description');
SET @brand_id = LAST_INSERT_ID();

INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item', 1000, 'item_image_url', 'item_description', @brand_id, NOW(), NOW());
SET @item_id = LAST_INSERT_ID();

INSERT INTO item_option (name, option_price, quantity, item_id)
VALUES ('Item Option', 100, 10, @item_id);
SET @itemOption_id = LAST_INSERT_ID();

INSERT INTO address (zip_code, road_name_address, address_details)
VALUES ('12345', '서울시 강남구', '상세주소');
-- 마지막으로 삽입된 address_id 값을 변수에 저장
SET @address_id = LAST_INSERT_ID();

INSERT INTO member (email, password, name, address_id)
VALUES ('test@naver.com', 'Asdf1234', '이름', @address_id);
-- 마지막으로 삽입된 member_id 값을 변수에 저장
SET @member_id = LAST_INSERT_ID();

INSERT INTO cart (member_id)
VALUES (@member_id);
SET @card_id = LAST_INSERT_ID();