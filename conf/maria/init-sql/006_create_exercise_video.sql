CREATE TABLE exercise_video (
                                exercise_video_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                exercise_name VARCHAR(255),
                                file_name VARCHAR(255),
                                created_at DATETIME,
                                foreign key (exercise_name) references exercise (exercise_name)

);
