create table user
(
    user_id                         BIGINT not null auto_increment primary key,
    username                        varchar(100) not null unique,
    password                        varchar(100) not null,
    nickname                        varchar(50) not null unique,
    height                          float not null,
    weight                          float not null,
    age                             integer not null,
    gender                          varchar(50) not null,
    role                            varchar(50),
    spicy_preference                integer,
    meat_consumption                bool,
    taste_preference                varchar(50),
    activity_level                  integer not null,
    preference_type_food            varchar(50),
    attendance_check                bool,
    preference_foods                varchar(200)



);


INSERT INTO user (username, password, nickname, height,
                  weight, age, gender, role, spicy_preference, meat_consumption, taste_preference,
                  activity_level, preference_type_food, attendance_check, preference_foods)
VALUES ('admin_username', '$2a$10$D1GNQmvoBt2nm7lj3Rbm/eY4SLSFEt585w9xEz0uhNJlMRJg1gzGG', 'Admin',
        175.5, 70.3, 30, 'male','ADMIN', 3, TRUE, '쓴맛', 3, '한식', FALSE, '짜장면,딸기,파스타');