
-- 카테고리 데이터 추가

INSERT INTO categories(category_name) VALUES('생활시설'), ('장애인용 화장실'), ('이동 보조 시설'), ('보행 장애물');

-- 생활시설과 관련된 세부 카테고리 추가
INSERT INTO detailed_categories (category_id, detailed_category_name) VALUES
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '음식점'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '카페'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '주점'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '미용실'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '편의점'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '마트'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '은행'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '숙박시설'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '휠체어 대여소'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '전동 휠체어 충전소'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '이동기기 수리센터');

-- 장애인용 화장실과 관련된 세부 카테고리 추가
INSERT INTO detailed_categories (category_id, detailed_category_name) VALUES
                                                                        ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '남성용'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '여성용'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '공용');

-- 이동 보조 시설과 관련된 세부 카테고리 추가
INSERT INTO detailed_categories (category_id, detailed_category_name) VALUES
                                                                        ((SELECT category_id FROM categories WHERE category_name = '이동 보조 시설'), '휠체어 리프트'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '이동 보조 시설'), '엘리베이터');

-- 보행 장애물과 관련된 세부 카테고리 추가
INSERT INTO detailed_categories (category_id, detailed_category_name) VALUES
                                                                        ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '보행 불가'),
                                                                        ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '보행 불편');

-- 생활시설과 관련된 태그 추가
INSERT INTO tags (category_id, tag_name, tag_accessibility_point) VALUES
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '출입구 경사', 10),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '문턱 없음', 15),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '장애인 화장실', 20),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '직원 서빙', 5),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '휠체어 리프트', 15),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '생활시설'), '엘리베이터', 15);

-- 장애인용 화장실과 관련된 태그 추가
INSERT INTO tags (category_id, tag_name, tag_accessibility_point) VALUES
                                                                      ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '휠체어 진입 가능', 20),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '기저귀 교환대', 10),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '장애인용 화장실'), '비상벨 있음', 15);

-- 이동 보조 시설과 관련된 태그 추가
INSERT INTO tags (category_id, tag_name, tag_accessibility_point) VALUES
                                                                      ((SELECT category_id FROM categories WHERE category_name = '이동 보조 시설'), '손잡이', 10),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '이동 보조 시설'), '안정적', 15);

-- 보행 장애물과 관련된 태그 추가
INSERT INTO tags (category_id, tag_name, tag_accessibility_point) VALUES
                                                                      ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '높은 단차', -10),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '좁은 보도폭', -15),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '급경사', -20),
                                                                      ((SELECT category_id FROM categories WHERE category_name = '보행 장애물'), '휠체어 진입 불가', -25);


INSERT INTO pois (code, deleted_at, poi_address, poi_latitude, poi_longitude, poi_name, poi_recent_update_date, sharable, detailed_category_id, user_id)
VALUES
-- 역삼동 (법정동 코드: 1168010800)
('1168010800', NULL, '서울특별시 강남구 역삼로 100', 37.499231, 127.037428, '장애물 1', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 101', 37.499771, 127.035564, '장애물 2', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 203', 37.503271, 127.038582, '장애물 3', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 204', 37.504155, 127.041112, '장애물 4', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 105', 37.498014, 127.036714, '장애물 5', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 105', 37.502874, 127.034547, '장애물 6', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 206', 37.503402, 127.042581, '장애물 7', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 106', 37.503145, 127.036047, '장애물 8', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 108', 37.497762, 127.034215, '장애물 9', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 209', 37.504789, 127.042148, '장애물 10', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 110', 37.496951, 127.034653, '장애물 11', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 112', 37.498671, 127.032984, '장애물 12', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 115', 37.496258, 127.035486, '장애물 13', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 212', 37.505971, 127.042879, '장애물 14', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 215', 37.506423, 127.043501, '장애물 15', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 115', 37.503863, 127.035087, '장애물 16', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 118', 37.495587, 127.036212, '장애물 17', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 218', 37.507325, 127.042968, '장애물 18', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 120', 37.497085, 127.032475, '장애물 19', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 120', 37.504512, 127.037124, '장애물 20', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 222', 37.508118, 127.044001, '장애물 21', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 125', 37.505689, 127.035827, '장애물 22', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 225', 37.508765, 127.042504, '장애물 23', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 125', 37.498147, 127.037512, '장애물 24', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 128', 37.497045, 127.033264, '장애물 25', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 228', 37.509453, 127.045039, '장애물 26', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 128', 37.504125, 127.036984, '장애물 27', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 역삼로 130', 37.496089, 127.034321, '장애물 28', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 논현로 130', 37.503098, 127.036017, '장애물 29', CURRENT_TIMESTAMP, true, 17, NULL),
('1168010800', NULL, '서울특별시 강남구 테헤란로 230', 37.509876, 127.046417, '장애물 30', CURRENT_TIMESTAMP, true, 17, NULL);
