ALTER TABLE captcha_codes
    MODIFY code VARCHAR (255) NOT NULL;

ALTER TABLE global_settings
    MODIFY code VARCHAR (255) NOT NULL;

ALTER TABLE users
    MODIFY email VARCHAR (255) NOT NULL;

ALTER TABLE posts
    MODIFY is_active SMALLINT NOT NULL;

ALTER TABLE users
    MODIFY is_moderator SMALLINT NOT NULL;

ALTER TABLE posts
    MODIFY moderation_status VARCHAR (255) NOT NULL;

ALTER TABLE global_settings
    MODIFY name VARCHAR (255) NOT NULL;

ALTER TABLE tags
    MODIFY name VARCHAR (255) NOT NULL;

ALTER TABLE users
    MODIFY name VARCHAR (255) NOT NULL;

ALTER TABLE users
    MODIFY password VARCHAR (255) NOT NULL;

ALTER TABLE post_comments
    MODIFY post_id INT NOT NULL;

ALTER TABLE post_votes
    MODIFY post_id INT NOT NULL;

ALTER TABLE tag2post
    MODIFY post_id INT NOT NULL;

ALTER TABLE users
    MODIFY reg_time datetime NOT NULL;

ALTER TABLE captcha_codes
    MODIFY secret_code VARCHAR (255) NOT NULL;

ALTER TABLE tag2post
    MODIFY tag_id INT NOT NULL;

ALTER TABLE post_comments
    MODIFY text VARCHAR (255) NOT NULL;

ALTER TABLE posts
    MODIFY text VARCHAR (255) NOT NULL;

ALTER TABLE captcha_codes
    MODIFY time datetime NOT NULL;

ALTER TABLE post_comments
    MODIFY time datetime NOT NULL;

ALTER TABLE post_votes
    MODIFY time datetime NOT NULL;

ALTER TABLE posts
    MODIFY time datetime NOT NULL;

ALTER TABLE post_comments
    MODIFY user_id INT NOT NULL;

ALTER TABLE post_votes
    MODIFY user_id INT NOT NULL;

ALTER TABLE posts
    MODIFY user_id INT NOT NULL;

ALTER TABLE global_settings
    MODIFY value VARCHAR (255) NOT NULL;

ALTER TABLE post_votes
    MODIFY value SMALLINT NOT NULL;

ALTER TABLE posts
    MODIFY view_count INT NOT NULL;