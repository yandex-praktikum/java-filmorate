--DROP TABLE FILMS, FILM_GENRE_LINK, FRIENDS, GENRES, RATING, MPA_RATING, USERS;

create table if not exists USERS
(
    ID_USER  BIGINT auto_increment,
    NAME     CHARACTER VARYING(20),
    LOGIN    CHARACTER VARYING(20) not null,
    EMAIL    CHARACTER VARYING(20) not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID_USER)
);

create table if not exists mpa_rating
(
    id_rate long,
    name varchar2(10),
    constraint MPA_RATING_PK
        primary key (id_rate)
);

create table if not exists films
(
    id_film long ,
    name varchar2(50) not null,
    description varchar2(200) not null,
    releasedate date,
    duration int,
	rate long,
    mpa long references mpa_rating(id_rate),
    constraint FILMS_PK
        primary key (id_film)
);

create table if not exists genres
(
    id_genre int,
    name varchar2(20),
    constraint GENRES_PK
        primary key (id_genre)
);

create table if not exists film_genre_link
(
    id_genre      long not null references GENRES(ID_GENRE),
    id_film       long not null references films(id_film),
    constraint FILM_GENRE_LINK_PK
        primary key (id_film,id_genre)
);



create table if not exists rating
(
    id_user long references USERS(ID_USER),
	id_film long references FILMS(ID_FILM),
    constraint RATING_PK
        primary key (id_user,id_film)
);

create table if not exists friends
(
    id_user long references USERS(ID_USER) on delete cascade ,
    id_friend long ,
    status int,
    constraint FRIENDS_PK
        primary key (id_user,id_friend)
)

