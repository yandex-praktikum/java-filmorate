package ru.yandex.practicum.filmorate.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT G.GENRE_ID, G.GENRE_NAME FROM GENRE AS G";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("GENRE_NAME")
            ));
        }
        return genres;
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        Genre genre = null;

        if (genreRows.next()) {
            genre = new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("GENRE_NAME")
            );
            log.info("Найден жанр: {}{}", genre.getId(), genre.getName());
        } else {
            log.info("Жанр с идентификатором: {} не найден", id);
            throw new ObjectNotFoundException("Указанного жанра не существует");
        }
        return genre;
    }
}
