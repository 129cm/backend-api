SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE item_cart;
TRUNCATE TABLE item_option;
TRUNCATE TABLE item;
TRUNCATE TABLE cart;
TRUNCATE TABLE member;
TRUNCATE TABLE address;
TRUNCATE TABLE brand;

SET FOREIGN_KEY_CHECKS = 1;

    -- Brand 데이터 삽입
INSERT INTO brand (name, image, description)
VALUES ('Brand1', 'image_url1', 'description1');
SET @brand_id1 = LAST_INSERT_ID();

-- Item 데이터 삽입
INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item1', 1000, 'item_image_url1', 'item_description1', @brand_id1, NOW(), NOW());
SET @item_id1 = LAST_INSERT_ID();

INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item2', 2000, 'item_image_url2', 'item_description2', @brand_id1, NOW(), NOW());
SET @item_id2 = LAST_INSERT_ID();

INSERT INTO item (name, price, image, description, brand_id, created_at, modified_at)
VALUES ('Item3', 3000, 'item_image_url3', 'item_description3', @brand_id1, NOW(), NOW());
SET @item_id3 = LAST_INSERT_ID();

-- ItemOption 데이터 삽입
INSERT INTO item_option (name, option_price, quantity, item_id)
VALUES ('Item Option1', 100, 10, @item_id1),
       ('Item Option2', 200, 20, @item_id2),
       ('Item Option3', 300, 30, @item_id3);

-- Address 데이터 삽입
INSERT INTO address (zip_code, road_name_address, address_details)
VALUES ('67890', '서울시 서초구', '상세주소1');
SET @address_id1 = LAST_INSERT_ID();

INSERT INTO address (zip_code, road_name_address, address_details)
VALUES ('67891', '서울시 강남구', '상세주소2');
SET @address_id2 = LAST_INSERT_ID();

INSERT INTO address (zip_code, road_name_address, address_details)
VALUES ('67892', '서울시 송파구', '상세주소3');
SET @address_id3 = LAST_INSERT_ID();

-- Member 데이터 삽입
INSERT INTO member (email, password, name, address_id)
VALUES ('test1@naver.com', 'Asdf1234', '이름1', @address_id1);
SET @member_id1 = LAST_INSERT_ID();

INSERT INTO member (email, password, name, address_id)
VALUES ('test2@naver.com', 'Qwer5678', '이름2', @address_id2);
SET @member_id2 = LAST_INSERT_ID();

INSERT INTO member (email, password, name, address_id)
VALUES ('test3@naver.com', 'Zxcv9012', '이름3', @address_id3);
SET @member_id3 = LAST_INSERT_ID();

-- Cart 데이터 삽입
INSERT INTO cart (member_id)
VALUES (@member_id1),
       (@member_id2),
       (@member_id3);

