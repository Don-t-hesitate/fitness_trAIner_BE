create table exercise
(
    exercise_id                     integer not null auto_increment primary key,
    exercise_name                   varchar(100) not null unique,
    per_kcal                        integer not null


);


INSERT INTO exercise (exercise_name, per_kcal)
VALUES ('푸쉬업', 8);
INSERT INTO exercise (exercise_name, per_kcal)
VALUES ('데드리프트', 12);
INSERT INTO exercise (exercise_name, per_kcal)
VALUES ('딥스', 25);
INSERT INTO exercise (exercise_name, per_kcal)
VALUES ('벤치프레스', 30);
INSERT INTO exercise (exercise_name, per_kcal)
VALUES ('숄더프레스', 15);