package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.ecxeption.OccurredException;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    private static final String CREATE_QUERY = """
            INSERT INTO films (name,description,releaseDate,duration) VALUES (?,?,?,?)
            """;
    private static final String UPDATE_QUERY = """
            UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE id = ?
            """;
    private static final String GET_ID_QUERY = """
            SELECT * FROM films WHERE id = ?
            """;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM films
            """;

    @Override
    public Film getFilmById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_ID_QUERY, new FilmRowMapper(jdbcTemplate), id);
        } catch (DataAccessException e) {
            throw new ValidationException("Такого фильма не существует!");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(GET_ALL_QUERY, new FilmRowMapper(jdbcTemplate));
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (film.getGenres() != null) {
            for (Genre genres : film.getGenres()) {
                try {
                    jdbcTemplate.update("INSERT INTO films_genre (films_id, genre_id) VALUES (?, ?)", film.getId(), genres.getId());
                    film.setGenres(new HashSet<>(film.getGenres()));
                } catch (DataAccessException e) {
                    throw new ValidationException("Такого жанра не существует!");
                }
            }
        }
        if (film.getMpa() != null) {
            try {
                jdbcTemplate.update("INSERT INTO films_rating (films_id, rating_id) VALUES (?, ?)", film.getId(), film.getMpa().getId());
                film.setMpa(film.getMpa());
            } catch (DataAccessException e) {
                throw new ValidationException("Такого рейтинга не существует!");
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            jdbcTemplate.queryForObject(GET_ID_QUERY, new FilmRowMapper(jdbcTemplate), film.getId());
        } catch (DataAccessException e) {
            throw new OccurredException("Такого фильма нет в списке!");
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(UPDATE_QUERY);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getId());
            return stmt;
        });
        return film;

    }

    public Film userLikesFilm(Long id, Long userId) {
        getFilmById(id);
        userDbStorage.getUserById(userId);
        jdbcTemplate.update("INSERT INTO films_likes (films_id, users_id) VALUES (?, ?)", id, userId);
        return getFilmById(id);
    }

    public Film deleteLikesFilm(Long id, Long userId) {
        getFilmById(id);
        userDbStorage.getUserById(userId);
        jdbcTemplate.update("DELETE FROM films_likes WHERE films_id=? AND users_id=?", id, userId);
        return getFilmById(id);
    }
}
