package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("dbFilms")
public class FilmDbStorage implements FilmStorage {
    private static final String SQL_FILMS_BASE = """
              SELECT f.id,
                     f.name,
                     f.description,
                     f.release_date,
                     f.duration,
                     mr.id   AS mpa_id,
                     mr.name AS mpa_name
              FROM films f
              LEFT JOIN mpa_ratings mr ON mr.id = f.mpa_rating_id
            """;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {

        String sql = "INSERT INTO FILMS (name, description, release_date, duration, mpa_rating_id) VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            if (film.getMpa() != null && film.getMpa().getId() != null) {
                ps.setInt(5, film.getMpa().getId());
            } else {
                ps.setObject(5, null);
            }
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        replaceFilmGenres(film.getId(), film.getGenres());
        return findById(film.getId()).orElseThrow();
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id=? WHERE id=?";

        int rows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
        if (rows == 0) {
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }

        replaceFilmGenres(film.getId(), film.getGenres());
        return findById(film.getId()).orElseThrow();
    }

    @Override
    public Optional<Film> findById(long id) {
        List<Film> films = jdbcTemplate.query(
                SQL_FILMS_BASE + " WHERE f.id = ?",
                (rs, rn) -> mapRowToFilm(rs),
                id
        );
        if (films.isEmpty()) return Optional.empty();
        loadGenresBulk(films);
        return Optional.of(films.get(0));
    }

    @Override
    public Collection<Film> findAll() {
        List<Film> films = jdbcTemplate.query(
                SQL_FILMS_BASE,
                (rs, rn) -> mapRowToFilm(rs)
        );
        loadGenresBulk(films);
        return films;
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM FILMS WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        if (rows == 0) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    @Override
    public Film addLike(long filmId, long userId) {
        final String sql = """
                INSERT INTO likes (film_id, user_id)
                SELECT ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM likes WHERE film_id = ? AND user_id = ?
                )
                """;

        jdbcTemplate.update(sql, filmId, userId, filmId, userId);

        return findById(filmId).orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        final String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
    }

    @Override
    public Collection<Film> findPopular(int count) {
        final String sql = """
                    SELECT f.id, f.name, f.description, f.release_date, f.duration,
                           mr.id AS mpa_id, mr.name AS mpa_name,
                           COUNT(l.user_id) AS likes_cnt
                    FROM films f
                    LEFT JOIN mpa_ratings mr ON mr.id = f.mpa_rating_id
                    LEFT JOIN likes l ON l.film_id = f.id
                    GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mr.id, mr.name
                    ORDER BY likes_cnt DESC, f.id
                    LIMIT ?
                """;
        List<Film> films = jdbcTemplate.query(sql, (rs, rn) -> mapRowToFilm(rs), count);
        loadGenresBulk(films);
        return films;
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .duration(rs.getInt("duration"))
                .likes(new HashSet<>())
                .build();

        Object mpaIdObj = rs.getObject("mpa_id");
        if (mpaIdObj != null) {
            film.setMpa(
                    MpaRating.builder()
                            .id(((Number) mpaIdObj).intValue())
                            .name(rs.getString("mpa_name"))
                            .build()
            );
        }
        film.setGenres(new LinkedHashSet<>());
        return film;
    }

    private void loadGenresBulk(List<Film> films) {
        if (films.isEmpty()) return;

        List<Long> ids = films.stream().map(Film::getId).toList();
        String inClause = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        String sql = """
                    SELECT fg.film_id, g.id AS genre_id, g.name AS genre_name
                    FROM film_genres fg
                    JOIN genres g ON g.id = fg.genre_id
                    WHERE fg.film_id IN (%s)
                    ORDER BY g.id
                """.formatted(inClause);

        Map<Long, Set<Genre>> byFilm = films.stream()
                .collect(Collectors.toMap(Film::getId, Film::getGenres));

        jdbcTemplate.query(sql, ids.toArray(), rs -> {
            long filmId = rs.getLong("film_id");
            Genre g = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build();
            byFilm.get(filmId).add(g);
        });
    }

    private void replaceFilmGenres(long filmId, Set<Genre> input) {
        List<Integer> ids = (input == null)
                ? List.of()
                : input.stream()
                .filter(g -> g != null && g.getId() != null)
                .map(Genre::getId)
                .distinct()
                .toList();

        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);

        if (!ids.isEmpty()) {
            List<Object[]> batch = ids.stream()
                    .map(id -> new Object[]{filmId, id})
                    .toList();
            jdbcTemplate.batchUpdate(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    batch
            );
        }
    }
}