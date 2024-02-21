/*
DROP TABLE IF EXISTS _users cascade ;
DROP TABLE IF EXISTS films cascade ;
DROP TABLE IF EXISTS friend_list cascade ;
DROP TABLE IF EXISTS user_likes cascade ;
DROP TABLE IF EXISTS genres cascade ;
DROP TABLE IF EXISTS film_genres cascade ;

-- Создание таблицы User
CREATE TABLE IF NOT EXISTS _users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    _name VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    birthday Date
    );

-- Создание таблицы Film
CREATE TABLE IF NOT EXISTS films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    _name VARCHAR(50),
    description VARCHAR(200),
    release_date DATE,
    duration INT,
    genre VARCHAR(20),
    mpa_rate VARCHAR(20)
);

-- Создание таблицы FriendList
CREATE TABLE IF NOT EXISTS friend_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    friend_id INT,
    friend_request_status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (friend_id) REFERENCES _users(id)
);

-- Создание таблицы UserLikes
CREATE TABLE IF NOT EXISTS user_likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    film_id INT,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (film_id) REFERENCES films(id)
);

create table if not exists genres (
  id int auto_increment primary key,
  name varchar(50)
);

CREATE TABLE IF NOT EXISTS film_genres (
  id INT AUTO_INCREMENT PRIMARY KEY,
  film_id INT,
  genre_id INT,
  FOREIGN KEY (film_id) REFERENCES films(id),
  FOREIGN KEY (genre_id) REFERENCES genres(id)
);*/
