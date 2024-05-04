CREATE TABLE user_score (
                            score_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            exercise_name VARCHAR(255),
                            score INT,
                            FOREIGN KEY (user_id) REFERENCES user (user_id)
);
