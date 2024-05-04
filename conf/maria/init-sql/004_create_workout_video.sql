CREATE TABLE workout_video (
                               workout_video_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               note_id BIGINT,
                               user_id BIGINT,
                               file_name VARCHAR(255),
                               exercise_name VARCHAR(255),
                               created_at DATETIME,
                               FOREIGN KEY (note_id) REFERENCES note (note_id),
                               FOREIGN KEY (user_id) REFERENCES user (user_id)
);
