create table user
(
    user_id                         integer not null auto_increment primary key,
    username                        varchar(100) not null unique,
    password                        varchar(100) not null,
    nickname                        varchar(50) not null unique,
    height                          float,
    weight                          float,
    age                             integer,
    role                            varchar(50),
    spicy_preference                integer,
    meat_consumption                bool,
    taste_preference                varchar(50),
    activity_level                  integer,
    preference_type_food            varchar(50),
    attendance_check                bool


);


INSERT INTO user (username, password, nickname, height,
                  weight, age, role, spicy_preference, meat_consumption, taste_preference,
                  activity_level, preference_type_food, attendance_check)
VALUES ('admin_username', '$2a$10$D1GNQmvoBt2nm7lj3Rbm/eY4SLSFEt585w9xEz0uhNJlMRJg1gzGG', 'Admin',
        175.5, 70.3, 30, 'ADMIN', 3, TRUE, '쓴맛', 3, '한식', FALSE);