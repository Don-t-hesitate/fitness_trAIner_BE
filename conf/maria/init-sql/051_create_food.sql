CREATE TABLE food (
    food_id VARCHAR(10) NOT NULL PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    main_type VARCHAR(50) NOT NULL,
    detail_type VARCHAR(50) NOT NULL,
    calories DECIMAL(6, 2) NOT NULL,
    protein DECIMAL(5, 2) NOT NULL,
    fat DECIMAL(5, 2) NOT NULL,
    carbohydr DECIMAL(5, 2) NOT NULL,
    taste VARCHAR(50) NOT NULL,
    main_ingred VARCHAR(50) NOT NULL,
    sec_ingred VARCHAR(50) NOT NULL,
    cook_method VARCHAR(50) NOT NULL
);

# csv 파일에서 음식 데이터를 읽어와서 데이터베이스에 저장
LOAD DATA INFILE '/home/t24108/aidata/food/food_db_result_initial.csv'
    INTO TABLE food
    FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@code, @name, @mainFoodType, @detailedFoodType, @calorie, @protein, @fat, @carbohydrate, @taste, @mainIngredient, @secondaryIngredient, @cookMethod)
    SET
        food_id = @code,
        food_name = @name,
        main_type = @mainFoodType,
        detail_type = @detailedFoodType,
        calories = @calorie,
        protein = @protein,
        fat = @fat,
        carbohydr = @carbohydrate,
        taste = @taste,
        main_ingred = @mainIngredient,
        sec_ingred = @secondaryIngredient,
        cook_method = @cookMethod;