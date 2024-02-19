INSERT INTO Users (username, login, email, birthday) VALUES
('John Smith', 'johnsmith', 'johnsmith@gmail.com', '1990-05-15'),
('Alice Johnson', 'alicejohnson', 'alicejohnson@gmail.com', '1995-12-02'),
('Michael Davis', 'michaeldavis', 'michaeldavis@gmail.com', '1988-09-20'),
('Emily Wilson', 'emilywilson', 'emilywilson@gmail.com', '1992-07-10'),
('Daniel Brown', 'danielbrown', 'danielbrown@gmail.com', '1998-04-25');

-- Заполнение таблицы Films
INSERT INTO Films (filmname, description, release_date, duration, genre, mpa_rate) VALUES
('The Shawshank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', '1994-09-23', 142, 'Drama', 'R'),
('The Godfather', 'An organized crime dynasty aging patriarch transfers control of his clandestine empire to his reluctant son.', '1972-03-24', 175, 'Crime', 'R'),
('Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', '1994-10-14', 154, 'Crime', 'R'),
('Fight Club', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much, much more.', '1999-10-15', 139, 'Drama', 'R'),
('The Dark Knight', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', '2008-07-18', 152, 'Action', 'PG-13');

-- Заполнение таблицы FriendList
INSERT INTO FriendList (user_id, friend_id, friend_request_status) VALUES
(1, 2, 'Accepted'),
(1, 3, 'Pending'),
(4, 1, 'Accepted'),
(5, 1, 'Pending'),
(2, 5, 'Accepted');

-- Заполнение таблицы UserLikes
INSERT INTO UserLikes (user_id, film_id) VALUES
(1, 1),
(1, 3),
(2, 2),
(3, 4),
(5, 5);