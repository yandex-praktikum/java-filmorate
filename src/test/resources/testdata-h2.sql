MERGE INTO MPA_RATINGS (id, name) KEY (id) VALUES (1, 'G'),
                                                  (2, 'PG'),
                                                  (3, 'PG-13'),
                                                  (4, 'R'),
                                                  (5, 'NC-17');

MERGE INTO GENRES (id, name) KEY (id) VALUES (1, 'Комедия'),
                                             (2, 'Драма'),
                                             (3, 'Мультфильм'),
                                             (4, 'Триллер'),
                                             (5, 'Документальный'),
                                             (6, 'Боевик');

INSERT INTO USERS (id, email, login, name, birthday)
VALUES (1, 'u1@example.com', 'user1', 'User One', DATE '1990-01-01');
