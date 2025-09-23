-- binary_contents 테이블 (참조 때문에 먼저 생성)
CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP    NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        VARBINARY    NOT NULL
);

-- users 테이블
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    profile_id UUID,
    CONSTRAINT fk_users_profile
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL
);

-- user_statuses 테이블
CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP,
    user_id        UUID      NOT NULL UNIQUE,
    last_active_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_statuses_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

-- channels 테이블
CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP   NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- read_statuses 테이블
CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP,
    user_id      UUID      NOT NULL,
    channel_id   UUID      NOT NULL,
    last_read_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_read_statuses_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT uk_read_statuses UNIQUE (user_id, channel_id)
);

-- messages 테이블
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    content    CLOB,
    channel_id UUID      NOT NULL,
    author_id  UUID,
    CONSTRAINT fk_messages_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_messages_author
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);

-- message_attachments 테이블 (다대다 관계)
CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_message_attachments_message
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE
);
