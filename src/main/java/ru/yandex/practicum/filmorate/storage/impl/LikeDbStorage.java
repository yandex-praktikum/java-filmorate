package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        String sql = "insert into likes (film_id, user_id) values (?, ?)";
        String sqlCounter = "update films set likes_counter = likes_counter + 1 where film_id = ?";

        jdbcTemplate.update(sql, filmId, userId);
        jdbcTemplate.update(sqlCounter, filmId);
        return true;
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        String sql = "delete from likes where film_id = ? and user_id = ?";
        String sqlCounter = "update films set likes_counter = likes_counter - 1 where film_id = ?";

        jdbcTemplate.update(sql, filmId, userId);
        jdbcTemplate.update(sqlCounter, filmId);
        return true;
    }

    @Override
    public List<Film> getFilmsWithMostLikes(Integer num) {
        String sql = "select film_id from films order by likes_counter desc limit ?";
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sql, num);

        List list = new ArrayList<>();

        while (likesRows.next()) {
            list.add(filmStorage.getFilm(likesRows.getLong("film_id")));
        }

        return list;
    }
}
