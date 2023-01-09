package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Genre> getById(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("unknown genre");
        }
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE ID_GENRE=?", id);
        if (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getLong("id_genre"),
                    genreRow.getString("name")
            );
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    public List<Optional<Genre>> getAll() {
        List<Optional<Genre>> list = new ArrayList<>();
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        while (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getLong("id_genre"),
                    genreRow.getString("name")
            );
            list.add(Optional.of(genre));
        }
        return list;
    }
}
