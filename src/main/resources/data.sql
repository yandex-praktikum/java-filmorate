-- MPA ratings
MERGE INTO mpa_ratings (id, name) KEY (id) VALUES (1, 'G');
MERGE INTO mpa_ratings (id, name) KEY (id) VALUES (2, 'PG');
MERGE INTO mpa_ratings (id, name) KEY (id) VALUES (3, 'PG-13');
MERGE INTO mpa_ratings (id, name) KEY (id) VALUES (4, 'R');
MERGE INTO mpa_ratings (id, name) KEY (id) VALUES (5, 'NC-17');

-- Genres
MERGE INTO genres (id, name) KEY (id) VALUES (1, 'Комедия');
MERGE INTO genres (id, name) KEY (id) VALUES (2, 'Драма');
MERGE INTO genres (id, name) KEY (id) VALUES (3, 'Мультфильм');
MERGE INTO genres (id, name) KEY (id) VALUES (4, 'Триллер');
MERGE INTO genres (id, name) KEY (id) VALUES (5, 'Документальный');
MERGE INTO genres (id, name) KEY (id) VALUES (6, 'Боевик');
