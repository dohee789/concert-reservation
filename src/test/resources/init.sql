INSERT INTO concert (name, venue) VALUES ('WORLD DJ FESTIVAL', '서울랜드'),
                                         ('Ultra Music Festival', '잠실 종합운동장');

INSERT INTO concert_schedule (concert_id, schedule_date_time) VALUES ((SELECT id FROM concert WHERE id = 1), CURRENT_TIMESTAMP),
                                                                    ((SELECT id FROM concert WHERE id = 2), CURRENT_TIMESTAMP);

INSERT INTO user (name) VALUES ('가'),('나'),('다'),('라'),('마');

INSERT INTO queue (user_id, token, queue_status, expired_at, entered_at)
VALUES ((SELECT id FROM user WHERE id = 1), 'abcd', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 2), 'abce', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 3), 'abcf', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 4), 'abcg', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 5), 'abch', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP);

INSERT INTO seat (seat_number, price, seat_status, concert_schedule_id)
VALUES (1, 100.0, 'AVAILABLE', 1),
       (2, 250.0, 'AVAILABLE', 1),
       (3, 100.0, 'AVAILABLE', 1),
       (4, 250.0, 'AVAILABLE', 1),
       (5, 200.0, 'AVAILABLE', 1);

INSERT INTO reservation (seat_id, user_id, reserved_at)
VALUES (1, (SELECT id FROM user WHERE id = 1), CURRENT_TIMESTAMP);

INSERT INTO wallet (user_id, balance, wallet_type, processed_at)
VALUES ((SELECT id FROM user WHERE id = 1), 50.0, 'PAYMENT', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 2), 100.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 3), 100.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 4), 100.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 5), 100.0, 'CHARGE', CURRENT_TIMESTAMP);
