INSERT INTO concert (name, venue) VALUES ('WORLD DJ FESTIVAL', '서울랜드'),
                                         ('Ultra Music Festival', '잠실 종합운동장');

INSERT INTO concert_schedule (concert_id, schedule_date_time) VALUES ((SELECT id FROM concert WHERE id = 1), CURRENT_TIMESTAMP),
                                                                    ((SELECT id FROM concert WHERE id = 2), CURRENT_TIMESTAMP);

INSERT INTO user (name) VALUES ('가'),('나'),('다'),('라'),('마');

INSERT INTO queue (user_id, token, queue_status, expired_at, entered_at)
VALUES ((SELECT id FROM user WHERE name = '가'), 'abcd', 'ACTIVE', CURRENT_TIMESTAMP+5, CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE name = '나'), 'abce', 'ACTIVE', CURRENT_TIMESTAMP+5, CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE name = '다'), 'abcf', 'ACTIVE', CURRENT_TIMESTAMP+5, CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE name = '라'), 'abcg', 'ACTIVE', CURRENT_TIMESTAMP+5, CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE name = '마'), 'abch', 'ACTIVE', CURRENT_TIMESTAMP+5, CURRENT_TIMESTAMP);

INSERT INTO seat (seat_number, price, seat_status, concert_schedule_id)
VALUES (1, 100.0, 'AVAILABLE', 1),
       (2, 250.0, 'AVAILABLE', 1),
       (3, 100.0, 'AVAILABLE', 1),
       (4, 250.0, 'AVAILABLE', 1),
       (5, 200.0, 'AVAILABLE', 1);

INSERT INTO reservation (seat_id, user_id, reserved_at)
VALUES (1, (SELECT id FROM user WHERE name = '가'), CURRENT_TIMESTAMP);
