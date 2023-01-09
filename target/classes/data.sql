delete from film_genre_link;
delete from films;
delete from friends;
delete from rating;
delete from users;
delete from mpa_rating;
delete from genres;

insert into MPA_RATING (ID_RATE,NAME)
values (1,'G');
insert into MPA_RATING (ID_RATE,NAME)
values (2,'PG');
insert into MPA_RATING (ID_RATE,NAME)
values (3,'PG-13');
insert into MPA_RATING (ID_RATE,NAME)
values (4,'R');
insert into MPA_RATING (ID_RATE,NAME)
values (5,'NC-17');
insert into GENRES (ID_GENRE,NAME)
values (1,'Комедия');
insert into GENRES (ID_GENRE,NAME)
values (2,'Драма');
insert into GENRES (ID_GENRE,NAME)
values (3,'Мультфильм');
insert into GENRES (ID_GENRE,NAME)
values (4,'Триллер');
insert into GENRES (ID_GENRE,NAME)
values (5,'Документальный');
insert into GENRES (ID_GENRE,NAME)
values (6,'Боевик');