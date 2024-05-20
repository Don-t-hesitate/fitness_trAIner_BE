CREATE TABLE diet (
    diet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    food_id VARCHAR(10) NOT NULL,
    eat_date DATE,
    total_calories DECIMAL(6, 2),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (food_id) REFERENCES food (food_id)
);
