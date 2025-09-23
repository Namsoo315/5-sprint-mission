-- users 데이터 3개
INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'alice',
        'alice@example.com', 'password1', NULL),
       ('22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'bob',
        'bob@example.com', 'password2', NULL),
       ('33333333-3333-3333-3333-333333333333', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'carol',
        'carol@example.com', 'password3', NULL);

-- user_statuses 데이터 3개
INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP),
       ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP),
       ('ccccccc3-cccc-cccc-cccc-ccccccccccc3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '33333333-3333-3333-3333-333333333333', CURRENT_TIMESTAMP);
