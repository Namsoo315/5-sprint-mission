CREATE TABLE `users`
(
    `id`         UUID NULL,
    `created_at` TIMESTAMPTZ  NOT NULL,
    `updated_at` TIMESTAMPTZ,
    `username`   VARCHAR(50)  NOT NULL,
    `email`      VARCHAR(100) NOT NULL,
    `password`   VARCHAR(60)  NOT NULL,
    `profile_id` UUID NULL
);

CREATE TABLE `binary_contents`
(
    `id`           UUID NULL,
    `created_at`   TIMESTAMPTZ  NOT NULL,
    `file_name`    VARCHAR(255) NOT NULL,
    `size`         BIGINT       NOT NULL,
    `content_type` VARCHAR(100) NOT NULL,
    `bytes`        BYTEA        NOT NULL
);

CREATE TABLE `user_statuses`
(
    `id`             UUID NULL,
    `created_at`     TIMESTAMPTZ NOT NULL,
    `updated_at`     TIMESTAMPTZ NULL,
    `user_id`        UUID NULL,
    `last_active_at` TIMESTAMPTZ NOT NULL
);

CREATE TABLE `read_statuses`
(
    `id`           UUID NULL,
    `created_at`   TIMESTAMPTZ NOT NULL,
    `updated_at`   TIMESTAMPTZ NULL,
    `user_id`      UUID NULL,
    `channel_id`   UUID NULL,
    `last_read_at` TIMESTAMPTZ NOT NULL
);

CREATE TABLE `channels`
(
    `id`          UUID NULL,
    `created_at`  TIMESTAMPTZ NOT NULL,
    `updated_at`  TIMESTAMPTZ NULL,
    `name`        VARCHAR(100) NULL,
    `description` VARCHAR(500) NULL,
    `type`        VARCHAR(10) NOT NULL
);

CREATE TABLE `messages`
(
    `id`         UUID NULL,
    `created_at` TIMESTAMPTZ NOT NULL,
    `updated_at` TIMESTAMPTZ NULL,
    `content`    TEXT NULL,
    `channel_id` UUID NULL,
    `author_id`  UUID NULL
);

CREATE TABLE `message_attachments`
(
    `message_id`     UUID NULL,
    `attachement_id` UUID NULL
);

ALTER TABLE `users`
    ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
                                           `id`
        );

ALTER TABLE `binary_contents`
    ADD CONSTRAINT `PK_BINARY_CONTENTS` PRIMARY KEY (
                                                     `id`
        );

ALTER TABLE `user_statuses`
    ADD CONSTRAINT `PK_USER_STATUSES` PRIMARY KEY (
                                                   `id`
        );

ALTER TABLE `read_statuses`
    ADD CONSTRAINT `PK_READ_STATUSES` PRIMARY KEY (
                                                   `id`
        );

ALTER TABLE `channels`
    ADD CONSTRAINT `PK_CHANNELS` PRIMARY KEY (
                                              `id`
        );

ALTER TABLE `messages`
    ADD CONSTRAINT `PK_MESSAGES` PRIMARY KEY (
                                              `id`
        );

