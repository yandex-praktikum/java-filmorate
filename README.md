# java-filmorate
![](https://github.com/dschinghis2008/java-filmorate/blob/add-database/bd_diagr.png)

SQL запросы проекта:
1. Получить все фильмы:  
SELECT * FROM films;  
2. Получить фильм по id:  
SELECT * FROM films WHERE id=?;  
3. Получить список популярных фильмов:  
SELECT f.id, f.name, f.description, f.release_date, f.duration, COUNT (l.user_id)  
FROM films f   
LEFT JOIN rating l ON f.id_film = l.id_film
GROUP BY f.id_film  
ORDER BY COUNT (l.id_user) DESC   
LIMIT ?;  
4. Получить всех пользователей:  
SELECT * FROM users;  
5. Получить пользователя по id:  
SELECT * FROM users WHERE id = ?;  
6. Получить список друзей пользователя по id:  
SELECT u.* FROM users u   
LEFT JOIN friends f ON f.id_friend = u.id_user   
WHERE f.id_user = ?;  
7. Получение списка всех жанров:  
SELECT * FROM genres;
8. Получить рейтинги MPA:
SELECT * FROM MPA_RATING