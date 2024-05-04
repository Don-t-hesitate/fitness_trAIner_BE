CREATE TABLE workout (
                         workout_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         note_id BIGINT NOT NULL,
                         exercise_name VARCHAR(255),
                         set_num INT,
                         repeats INT,
                         weight INT,
                     foreign key (note_id) references note (note_id)
);
