package ru.yandex.practicum.filmorate.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.filmorate.model.Film;
import ru.yandex.practicum.filmorate.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT F.FILM_ID,F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION " +
                "FROM FILM AS F";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            films.add(getFilmById(filmRows.getInt("FILM_ID")));
        }
        return films;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT F.FILM_ID,F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION " +
                "FROM FILM AS F " +
                "WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        Film film = null;

        if (filmRows.next()) {
            film = new Film(
                    filmRows.getInt("FILM_ID"),
                    filmRows.getString("FILM_NAME"),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getInt("DURATION"),
                    getGenreByFilmId(id),
                    getMpaByFilmId(id)
            );
            log.info("Найден фильм: {}{}", film.getId(), film.getName());
        } else {
            log.info("Фильм с идентификатором: {} не найден", id);
            throw new ObjectNotFoundException("Указанного фильма не существует");
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно быть не более 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM")
                .usingGeneratedKeyColumns("FILM_ID");
        simpleJdbcInsert.execute(film.toMap());
        assignIdForFilm(film);
        if (film.getGenres() != null) {
            createRowFILM_GENRE(film);
            assignGenre(film);
        }
        assignMpa(film);
        addFilmInLikeTable(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlId = "SELECT FILM_ID FROM FILM WHERE FILM_ID = ?";
        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlId, film.getId());
        if (idRows.next()) {
            String sql = "update FILM set " +
                    "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                    "where FILM_ID = ?";
            jdbcTemplate.update(sql
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId()
            );
            assignMpa(film);

            if (film.getGenres() != null) {
                createRowFILM_GENRE(film);
                assignGenre(film);
                List<Genre> genres = film.getGenres().stream().distinct().collect(Collectors.toList());
                film.setGenres(genres);
            } else {
                deleteGenreForFilm(film);
            }
        } else {
            throw new ObjectNotFoundException("Указанного фильма не существует");
        }
        return film;
    }


    private void assignMpa(Film film) {
        if (film.getMpa().getName() == null) {
            String sql = "SELECT RATING_NAME FROM RATING WHERE RATING_ID = ?";
            SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sql, film.getMpa().getId());
            if (mpaRow.next()) {
                film.getMpa().setName(mpaRow.getString("RATING_NAME"));
            }
        }
    }

    private void assignGenre(Film film) {
        String sql = "SELECT GENRE_NAME FROM GENRE WHERE GENRE_ID = ?";
        for (Genre genre : film.getGenres()) {
            SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sql, genre.getId());
            if (mpaRow.next()) {
                genre.setName(mpaRow.getString("GENRE_NAME"));
            }
        }
    }

    private void createRowFILM_GENRE(Film film) {
        if (!film.getGenres().isEmpty()) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("FILM_GENRE")
                    .usingGeneratedKeyColumns("FILM_GENRE_ID");
            List<Genre> genres = film.getGenres().stream().distinct().collect(Collectors.toList());
            deleteGenreForFilm(film);
            Map<String, Object> genre_film = new HashMap<>();
            for (Genre genre : genres) {
                if (!checkRatingFilmDuplicate(film, genre)) {
                    genre_film.put("FILM_ID", film.getId());
                    genre_film.put("GENRE_ID", genre.getId());
                    simpleJdbcInsert.execute(genre_film);
                }
            }
        } else {
            String sql = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
            jdbcTemplate.update(sql, film.getId());
        }
    }

    private void deleteGenreForFilm(Film film) {
        String sql = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }


    private List<Genre> getGenreByFilmId(int id) {
        String sql = "SELECT FG.GENRE_ID, G.GENRE_NAME\n" +
                "      FROM FILM AS F\n" +
                "      JOIN FILM_GENRE as FG on F.FILM_ID = FG.FILM_ID\n" +
                "      JOIN GENRE as G on FG.GENRE_ID = G.GENRE_ID\n" +
                "      WHERE F.film_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("GENRE_NAME"));
            genres.add(genre);
        }
        SqlRowSet genreRows1 = jdbcTemplate.queryForRowSet(sql, id);
        if (!genreRows1.next()) {
            genres = null;
        }
        return genres;
    }

    private Mpa getMpaByFilmId(int id) {
        String sql = "SELECT R.RATING_ID, R.RATING_NAME\n" +
                "FROM FILM AS F\n" +
                "JOIN RATING R on F.RATING_ID = R.RATING_ID WHERE F.film_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql, id);
        Mpa mpa = null;
        if (mpaRows.next()) {
            mpa = new Mpa(
                    mpaRows.getInt("RATING_ID"),
                    mpaRows.getString("RATING_NAME")
            );
        }
        return mpa;
    }

    private void assignIdForFilm(Film film) {
        if (film.getId() == 0) {
            String sqlForId = "SELECT FILM_ID\n" +
                    "FROM Film\n" +
                    "WHERE FILM_NAME = ?\n" +
                    "    AND DESCRIPTION = ?\n" +
                    "    AND RELEASE_DATE = ?\n" +
                    "    AND DURATION = ?\n" +
                    "    AND RATING_ID = ?";
            SqlRowSet filmIdRows = jdbcTemplate.queryForRowSet(sqlForId,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId());
            if (filmIdRows.next()) {
                film.setId(filmIdRows.getInt("FILM_ID"));
            }
        }
    }

    private void addFilmInLikeTable(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("LIKES")
                .usingGeneratedKeyColumns("LIKE_ID");
        Map<String, Object> map = new HashMap<>();
        map.put("FILM_ID", film.getId());
        simpleJdbcInsert.execute(map);
    }

    private boolean checkRatingFilmDuplicate(Film film, Genre genre) {
        String sql = "SELECT GENRE_ID, FILM_ID FROM FILM_GENRE WHERE FILM_ID = ? and GENRE_ID = ?";
        SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet(sql, film.getId(), genre.getId());
        return filmGenreRows.next();
    }
}
