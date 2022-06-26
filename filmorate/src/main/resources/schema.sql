create table IF NOT EXISTS genre
(
    genre_id   int generated by default as identity,
    genre_name varchar(30)
    );

create table IF NOT EXISTS rating
(
    rating_id   int generated by default as identity,
    rating_name varchar(30)
    );

create table IF NOT EXISTS film
(
    film_id     int generated by default as identity,
    film_name   varchar(30),
    description varchar(200),
    release_date date,
    duration    int,
    rating_id int REFERENCES rating(rating_id)
    );

create table IF NOT EXISTS film_genre
(
    film_genre_id int generated by default as identity,
    film_id  int REFERENCES film (film_id),
    genre_id int REFERENCES genre (genre_id)
    );

create table IF NOT EXISTS user
(
    user_id  int generated by default as identity,
    user_name     varchar(30),
    email    varchar(30) NOT NULL,
    login    varchar(30) NOT NULL,
    birthday date
    );

create table IF NOT EXISTS likes
(
    like_id int generated by default as identity,
    user_id int REFERENCES user (user_id),
    film_id int REFERENCES film (film_id)
    );

create table IF NOT EXISTS status
(
    status_id   int generated by default as identity,
    status_name varchar(30)
    );

create table IF NOT EXISTS relationship
(
    relationship_id int generated by default as identity,
    user_id         int REFERENCES user (user_id),
    friend_id       int REFERENCES user (user_id),
    status_id       int REFERENCES status (status_id)
    );



