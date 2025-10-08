package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbcTemplate;

    private static final String GENRES_BY_FILM_QUERY = """
                SELECT g.id, g.name
                FROM genre g
                JOIN films_genre fg ON g.id = fg.genre_id
                WHERE fg.films_id = ?
            """;

    private static final String LIKES_BY_FILM_QUERY = """
                SELECT fl.users_id
                FROM films_likes fl
                WHERE fl.films_id = ?
            """;

    private static final String RATING_BY_FILM_QUERY = """
                SELECT r.id, r.name
                FROM rating r
                JOIN films_rating fr ON r.id = fr.rating_id
                WHERE fr.films_id = ?
            """;


    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));

        List<Genre> genres = jdbcTemplate.query(GENRES_BY_FILM_QUERY, new GenreRowMapper(), film.getId());
        genres.sort(Comparator.comparing(Genre::getId));
        film.setGenres(new LinkedHashSet<>(genres));

        List<Long> users = jdbcTemplate.queryForList(LIKES_BY_FILM_QUERY, Long.class, film.getId());
        film.setLikes(new HashSet<>(users));

        List<Rating> rating = jdbcTemplate.query(RATING_BY_FILM_QUERY, new RatingRowMapper(), film.getId());
        if (rating.isEmpty()) {
            return film;
        }
        film.setMpa(rating.getFirst());
        return film;
    }
}
