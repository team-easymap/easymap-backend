
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
