-- 1. User 테이블 생성 (user 테이블이 다른 테이블들의 참조 대상이므로 가장 먼저 생성해야 합니다.)
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- 2. Concert 테이블 생성
CREATE TABLE IF NOT EXISTS `concert` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    venue VARCHAR(255) NOT NULL
);

-- 3. ConcertSchedule 테이블 생성 (concert_id를 참조하므로 concert 테이블이 먼저 생성되어야 합니다.)
CREATE TABLE IF NOT EXISTS `concert_schedule` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    concert_id BIGINT NOT NULL,
    schedule_date_time TIMESTAMP NOT NULL,
    opened_at TIMESTAMP NOT NULL,
    FOREIGN KEY (concert_id) REFERENCES `concert`(id) ON DELETE CASCADE
);

-- 4. Seat 테이블 생성 (concert_schedule_id를 참조하므로 concert_schedule 테이블이 먼저 생성되어야 합니다.)
CREATE TABLE IF NOT EXISTS `seat` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number BIGINT NOT NULL,
    price FLOAT NOT NULL,
    seat_status VARCHAR(255) NOT NULL,
    concert_schedule_id BIGINT NOT NULL,
    FOREIGN KEY (concert_schedule_id) REFERENCES `concert_schedule`(id) ON DELETE CASCADE,
    UNIQUE (concert_schedule_id, seat_number),
    INDEX idx_concert_schedule_id(concert_schedule_id),
    INDEX idx_seat_status(seat_status),
    version BIGINT DEFAULT 0 NOT NULL
);

-- 5. Queue 테이블 생성 (user_id를 참조하므로 user 테이블이 먼저 생성되어야 합니다.)
CREATE TABLE IF NOT EXISTS `queue` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    queue_status VARCHAR(255) NOT NULL,
    expired_at TIMESTAMP,
    entered_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE,
    INDEX idx_queue_user_id(user_id),
    INDEX idx_queue_token(token),
    INDEX idx_queue_queue_status(queue_status),
    version BIGINT DEFAULT 0 NOT NULL
);

-- 6. Reservation 테이블 생성 (seat_id, user_id를 참조하므로 seat와 user 테이블이 먼저 생성되어야 합니다.)
CREATE TABLE IF NOT EXISTS `reservation` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reserved_at TIMESTAMP NOT NULL,
    FOREIGN KEY (seat_id) REFERENCES `seat`(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE,
    UNIQUE (seat_id, user_id),
    INDEX idx_user_id(user_id),
    INDEX idx_reserved_at(reserved_at)
);

-- 7. Wallet 테이블 생성 (user_id를 참조하므로 user 테이블이 먼저 생성되어야 합니다.)
CREATE TABLE IF NOT EXISTS `wallet` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance FLOAT,
    wallet_type VARCHAR(255),
    processed_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE,
    INDEX idx_user_id(user_id),
    INDEX idx_processed_at(processed_at)
);

-- 8. WalletHistory
CREATE TABLE IF NOT EXISTS `wallet_history` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    CONSTRAINT fk_wallet_history_wallet_id FOREIGN KEY (wallet_id)
        REFERENCES wallet(id)
        ON DELETE CASCADE
);


INSERT INTO concert (name, venue) VALUES ('WORLD DJ FESTIVAL', '서울랜드'),
                                         ('Ultra Music Festival', '잠실 종합운동장');

INSERT INTO concert_schedule (concert_id, schedule_date_time, opened_at) VALUES ((SELECT id FROM concert WHERE id = 1), '2025-05-15 20:00:00', '2025-05-25 20:00:00'),
                                                                                ((SELECT id FROM concert WHERE id = 2), '2025-06-15 20:00:00', '2025-06-25 20:00:00');

INSERT INTO user (name) VALUES ('가'),('나'),('다'),('라'),('마');

INSERT INTO queue (user_id, token, queue_status, expired_at, entered_at)
VALUES ((SELECT id FROM user WHERE id = 1), 'abcd', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 2), 'abce', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 3), 'abcf', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 4), 'abcg', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 5), 'abch', 'ACTIVE', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE), CURRENT_TIMESTAMP);

INSERT INTO seat (seat_number, price, seat_status, concert_schedule_id)
VALUES (1, 100.0, 'AVAILABLE', 1),
       (2, 250.0, 'AVAILABLE', 2),
       (3, 100.0, 'AVAILABLE', 1),
       (4, 250.0, 'AVAILABLE', 1),
       (5, 200.0, 'AVAILABLE', 2);

INSERT INTO reservation (seat_id, user_id, reserved_at)
VALUES (1, (SELECT id FROM user WHERE id = 1), CURRENT_TIMESTAMP);

INSERT INTO wallet (user_id, balance, wallet_type, processed_at)
VALUES ((SELECT id FROM user WHERE id = 1), 100.0, 'PAYMENT', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 2), 300.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 3), 100.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 4), 100.0, 'CHARGE', CURRENT_TIMESTAMP),
        ((SELECT id FROM user WHERE id = 5), 100.0, 'CHARGE', CURRENT_TIMESTAMP);

-----동시성 테스트를 위한 user insert (도커에서 직접 접속 후 실행 -> 현재 세션에만 적용됨. 재시작하거나 세션이 종료되면 사라짐)
--SET max_execution_time = 0;
--SET @@cte_max_recursion_depth = 5000;

--INSERT INTO `user` (id, name)
--SELECT n, CONCAT('User', n)
--FROM (
--    WITH RECURSIVE seq AS (
--        SELECT 1 AS n
--        UNION ALL
--        SELECT n + 1 FROM seq WHERE n < 4000
--    )
--    SELECT n FROM seq
--) AS numbers;
