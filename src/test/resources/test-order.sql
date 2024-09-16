-- 브랜드(Brand) 더미 데이터 삽입
INSERT INTO brand (name, description, image) VALUES
                                                 ('브랜드A', '브랜드 A 설명', 'brandA_image.jpg'),
                                                 ('브랜드B', '브랜드 B 설명', 'brandB_image.jpg'),
                                                 ('브랜드C', '브랜드 C 설명', 'brandC_image.jpg');

-- 회원(Member) 더미 데이터 삽입
INSERT INTO member (email, name, password) VALUES
                                               ('user1@example.com', '홍길동', 'password1'),
                                               ('user2@example.com', '김철수', 'password2'),
                                               ('user3@example.com', '이영희', 'password3'),
                                               ('user4@example.com', '박준영', 'password4'),
                                               ('user5@example.com', '최민수', 'password5');

-- 주소(Address) 더미 데이터 삽입
INSERT INTO address (member_id, address_details, road_name_address, zip_code) VALUES
                                                                                  (1, '서울시 강남구', '강남대로 123', '06053'),
                                                                                  (2, '부산시 해운대구', '해운대로 456', '48094'),
                                                                                  (3, '대구시 달서구', '달서대로 789', '42610'),
                                                                                  (4, '인천시 남동구', '남동대로 101', '21544'),
                                                                                  (5, '광주시 서구', '서구대로 202', '61950');

-- 장바구니(Cart) 더미 데이터 삽입
INSERT INTO cart (member_id) VALUES
                                 (1),
                                 (2),
                                 (3),
                                 (4),
                                 (5);

-- 아이템(Item) 더미 데이터 삽입
INSERT INTO item (name, price, description, image, brand_id, created_at, modified_at) VALUES
                                                                                          ('아이템A', 10000, '아이템 A 설명', 'itemA_image.jpg', 1, NOW(), NOW()),
                                                                                          ('아이템B', 20000, '아이템 B 설명', 'itemB_image.jpg', 2, NOW(), NOW()),
                                                                                          ('아이템C', 30000, '아이템 C 설명', 'itemC_image.jpg', 3, NOW(), NOW());

-- 아이템 옵션(ItemOption) 더미 데이터 삽입
INSERT INTO item_option (name, quantity, option_price, item_id, deleted) VALUES
                                                                             ('옵션A1', 10, 500, 1, 0),
                                                                             ('옵션A2', 5, 300, 1, 0),
                                                                             ('옵션B1', 8, 700, 2, 0),
                                                                             ('옵션C1', 12, 1000, 3, 0);

-- 아이템 장바구니(ItemCart) 더미 데이터 삽입
INSERT INTO item_cart (count, cart_id, item_id, item_option_id) VALUES
                                                                    (2, 1, 1, 1),
                                                                    (1, 2, 2, 3),
                                                                    (3, 3, 3, 4),
                                                                    (2, 4, 1, 2),
                                                                    (1, 5, 2, 3);

-- 주문(Order) 더미 데이터 삽입
-- 총 10개의 주문 데이터를 추가
INSERT INTO orders (created_at, modified_at, member_id, code_id, group_id, order_serial) VALUES
                                                                                             (NOW(), NOW(), 1, '010', '100', 'ORD123456'),
                                                                                             (NOW(), NOW(), 2, '020', '100', 'ORD123457'),
                                                                                             (NOW(), NOW(), 3, '020', '100', 'ORD123458'),
                                                                                             (NOW(), NOW(), 4, '030', '100', 'ORD123459'),
                                                                                             (NOW(), NOW(), 5, '010', '100', 'ORD123460'),
                                                                                             (NOW(), NOW(), 1, '020', '100', 'ORD123461'),
                                                                                             (NOW(), NOW(), 2, '020', '100', 'ORD123462'),
                                                                                             (NOW(), NOW(), 3, '030', '100', 'ORD123463'),
                                                                                             (NOW(), NOW(), 4, '010', '100', 'ORD123464'),
                                                                                             (NOW(), NOW(), 5, '020', '100', 'ORD123465');

-- 주문 항목 옵션(OrderItemOption) 더미 데이터 삽입
INSERT INTO order_item_option (count, sales_price, created_at, modified_at, item_option_id, order_id) VALUES
                                                                                                          (2, 1000, NOW(), NOW(), 1, 1),
                                                                                                          (2, 1000, NOW(), NOW(), 2, 1),
                                                                                                          (1, 700, NOW(), NOW(), 3, 2),
                                                                                                          (3, 1200, NOW(), NOW(), 4, 3),
                                                                                                          (1, 1300, NOW(), NOW(), 1, 4),
                                                                                                          (2, 1500, NOW(), NOW(), 2, 5),
                                                                                                          (1, 700, NOW(), NOW(), 3, 6),
                                                                                                          (3, 1200, NOW(), NOW(), 4, 7),
                                                                                                          (2, 1500, NOW(), NOW(), 2, 8),
                                                                                                          (1, 700, NOW(), NOW(), 3, 9),
                                                                                                          (3, 1200, NOW(), NOW(), 4, 10);

-- 파트너스(Partners) 더미 데이터 삽입
INSERT INTO partners (email, password, business_number, brand_id) VALUES
                                                                      ('partnerA@company.com', 'partner_passwordA', '1234567890', 1),
                                                                      ('partnerB@company.com', 'partner_passwordB', '0987654321', 2),
                                                                      ('partnerC@company.com', 'partner_passwordC', '1122334455', 3);
