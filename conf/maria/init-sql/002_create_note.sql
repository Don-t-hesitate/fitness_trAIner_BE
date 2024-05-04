CREATE TABLE note (
                      note_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      workout_date DATE,
                      total_score INT,
                      total_kcal INT,
                      total_perfect INT,
                      total_good INT,
                      total_bad INT,
                      FOREIGN KEY (user_id) REFERENCES user (user_id)
);
