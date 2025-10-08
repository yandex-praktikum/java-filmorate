package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE_QUERY = """
            INSERT INTO genre (name) VALUES (?)
            """;
    private static final String GET_ID_QUERY = """
            SELECT * FROM genre WHERE id = ?
            """;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM genre
            """;

    @Override
    public Genre createGenre(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        genre.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return genre;
    }

    @Override
    public Genre getGenreById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_ID_QUERY, new GenreRowMapper(), id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Такого жанра нет!");
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_ALL_QUERY, new GenreRowMapper());
    }
}
