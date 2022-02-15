CREATE TABLE captcha_codes
(
    id          INT AUTO_INCREMENT NOT NULL,
    time        datetime     NULL,
    code        VARCHAR(255) NULL,
    secret_code VARCHAR(255) NULL,
    CONSTRAINT pk_captcha_codes PRIMARY KEY (id)
);

CREATE TABLE global_settings
(
    id    INT AUTO_INCREMENT NOT NULL,
    code  VARCHAR(255) NULL,
    name  VARCHAR(255) NULL,
    value VARCHAR(255) NULL,
    CONSTRAINT pk_global_settings PRIMARY KEY (id)
);

CREATE TABLE post_comments
(
    id        INT AUTO_INCREMENT NOT NULL,
    parent_id INT          NULL,
    post_id   INT          NULL,
    user_id   INT          NULL,
    time      datetime     NULL,
    text      TEXT     NULL,
    CONSTRAINT pk_post_comments PRIMARY KEY (id)
);

CREATE TABLE post_votes
(
    id      INT AUTO_INCREMENT NOT NULL,
    user_id INT      NULL,
    post_id INT      NULL,
    time    datetime NULL,
    value   SMALLINT NULL,
    CONSTRAINT pk_post_votes PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id                INT AUTO_INCREMENT NOT NULL,
    is_active         SMALLINT     NULL,
    moderation_status VARCHAR(255) NULL,
    moderator_id      INT          NULL,
    user_id           INT          NULL,
    time              datetime     NULL,
    text              TEXT      NULL,
    view_count        INT          NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE tag2post
(
    id      INT AUTO_INCREMENT NOT NULL,
    post_id INT NULL,
    tag_id  INT NULL,
    CONSTRAINT pk_tag2post PRIMARY KEY (id)
);

CREATE TABLE tags
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

CREATE TABLE users
(
    id           INT AUTO_INCREMENT NOT NULL,
    is_moderator SMALLINT     NULL,
    reg_time     datetime     NULL,
    name         VARCHAR(255) NULL,
    email        VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    code         VARCHAR(255) NULL,
    photo        VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_MODERATOR FOREIGN KEY (moderator_id) REFERENCES users (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_PARENT FOREIGN KEY (parent_id) REFERENCES post_comments (id);

ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_comments
    ADD CONSTRAINT FK_POST_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE post_votes
    ADD CONSTRAINT FK_POST_VOTES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE post_votes
    ADD CONSTRAINT FK_POST_VOTES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);