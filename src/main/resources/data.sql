INSERT INTO _users (_name, login, email, birthday)
VALUES ('John Smith', 'johnsmith', 'johnsmith@gmail.com', '1990-05-15'),
       ('Alice Johnson', 'alicejohnson', 'alicejohnson@gmail.com', '1995-12-02'),
       ('Michael Davis', 'michaeldavis', 'michaeldavis@gmail.com', '1988-09-20'),
       ('Emily Wilson', 'emilywilson', 'emilywilson@gmail.com', '1992-07-10'),
       ('Daniel Brown', 'danielbrown', 'danielbrown@gmail.com', '1998-04-25'),
       ('Иван Петров', 'ivanpetrov', 'ivanpetrov@gmail.com', '1993-11-12'),
       ('Елена Смирнова', 'elenasmirnova', 'elenasmirnova@gmail.com', '1987-08-25'),
       ('Сергей Иванов', 'sergeiivanov', 'sergeiivanov@gmail.com', '1990-06-30'),
       ('Мария Козлова', 'mariakozlova', 'mariakozlova@gmail.com', '1995-04-17'),
       ('Александр Николаев', 'alexandrnikolaev', 'alexandrnikolaev@gmail.com', '1984-12-05'),
       ('Ольга Егорова', 'olgaegorova', 'olgaegorova@gmail.com', '1999-01-28'),
       ('Дмитрий Морозов', 'dmitriymorozov', 'dmitriymorozov@gmail.com', '1992-10-14'),
       ('Татьяна Лебедева', 'tatyanalebedeva', 'tatyanalebedeva@gmail.com', '1996-07-08'),
       ('Павел Соколов', 'pavelsokolov', 'pavelsokolov@gmail.com', '1989-03-22'),
       ('Анна Кузнецова', 'annakuznetsova', 'annakuznetsova@gmail.com', '1997-02-19');

-- Заполнение таблицы Films
INSERT INTO films (_name, description, release_date, duration, genre, mpa_rate)
VALUES ('Матрица', 'Киберпанк боевик о борьбе человека с машинами', '1999-03-28', 136, 1, 'R'),
       ('Терминатор 2: Судный День', 'Боевик о борьбе человека с технологическими угрозами', '1991-07-03', 137, 2, 'R'),
       ('Властелин Колец: Возвращение Короля', 'Эпическое фэнтезийное приключение о борьбе за Средиземье', '2003-01-01',
        201, 3, 'PG-13'),
       ('Бриллиантовая Рука', 'Комедия о криминальной жизни СССР', '1968-12-17', 111, 4, 'PG'),
       ('Титаник', 'Романтическая драма о затонувшем лайнере', '1997-12-19', 195, 5, 'PG-13'),
       ('Гладиатор', 'Исторический боевик о гладиаторах в Древнем Риме', '2000-05-05', 155, 6, 'R'),
       ('Парк Юрского Периода', 'Научно-фантастический боевик о восстановлении динозавров', '1993-06-11', 127, 7,
        'PG-13'),
       ('Джентльмены удачи', 'Юмористическая комедия о преступниках в СССР', '1971-04-05', 84, 8, 'PG'),
       ('Форрест Гамп', 'Драма о необычной судьбе простого человека', '1994-07-07', 142, 9, 'PG-13'),
       ('Достучаться до небес', 'Комедия о приключениях российской гопоты', '1997-09-21', 88, 10, 'R');

-- Заполнение таблицы FriendList
INSERT INTO friend_list (user_id, friend_id, friend_request_status)
VALUES (1, 2, 'CONFIRMED'),
       (2, 1, 'CONFIRMED'),
       (1, 3, 'CONFIRMED'),
       (3, 1, 'CONFIRMED'),
       (2, 5, 'CONFIRMED'),
       (5, 2, 'CONFIRMED'),
       (5, 1, 'UNCONFIRMED'),
       (5, 3, 'UNCONFIRMED'),
       (5, 4, 'UNCONFIRMED');

-- Заполнение таблицы UserLikes
INSERT INTO user_likes (user_id, film_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 2),
       (6, 2),
       (7, 2),
       (8, 2),
       (9, 2),
       (10, 3),
       (11, 3),
       (12, 3),
       (13, 4),
       (14, 4),
       (15, 4),
       (1, 4),
       (2, 4),
       (3, 4),
       (4, 4),
       (5, 5),
       (6, 5),
       (7, 5),
       (8, 5),
       (9, 5),
       (10, 5),
       (11, 5),
       (12, 5),
       (13, 5);

INSERT INTO genres (name)
values ('Комедия'),
       ('Драма'),
       ('Фантастика'),
       ('Боевик'),
       ('Ужасы'),
       ('Мелодрама'),
       ('Триллер'),
       ('Фэнтези'),
       ('Документальный'),
       ('Криминал'),
       ('Приключенческий'),
       ('Вестерн');

insert into film_genres(film_id, genre_id)
values (1, 3),
       (1, 4),
       (2, 3),
       (2, 4),
       (3, 8),
       (4, 1),
       (5, 2),
       (6, 4),
       (6, 10),
       (7, 3),
       (7, 11),
       (8, 2),
       (9, 1),
       (9, 6),
       (10, 1);


