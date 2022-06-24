package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select  film_id, f.name as fname, description, releaseDate, duration, " +
                "f.rating_id as frating_id, rm.name as rmname " +
                "from films as f join rating_mpa as rm on f.rating_id = rm.rating_id";
        String sqlGenres = "select film_id, fg.GENRE_ID as fggenre_id, g.name as gname\n" +
                "from film_genres as fg join genres as g on fg. genre_id = g. genre_id\n";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenres);
        Map<Long, Set<Genre>> genreMap = makeGenreMap(genreRows);

        List<Film> filmWithGenres = new ArrayList<>();
        for (Film film : films) {
            filmWithGenres.add(new Film(film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa(),
                    genreMap.getOrDefault(film.getId(), null)));
        }

        return filmWithGenres;
    }

    /**
     * Маппер
     */
    private Film makeFilm(ResultSet rs) throws SQLException {
        Long id = rs.getLong("film_id");
        String name = rs.getString("fname");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        Integer duration = rs.getInt("duration");
        RatingMPA mpa = new RatingMPA(rs.getLong("frating_id"), rs.getString("rmname"));

        return new Film(id, name, description, releaseDate, duration, mpa, null);
    }

    /**
     * Преобразует данные таблицы film_genres в HashMap<Long, Set<Genre>>, где Long - id фильма,
     * Set<Genre> - сет из жанров фильма с ключем-id для помещения в объект film.
     * @param genresRowSet
     * @return
     */
    private Map<Long, Set<Genre>> makeGenreMap(SqlRowSet genresRowSet) {
        Map<Long, Set<Genre>> genreMap = new HashMap<>();
        while (genresRowSet.next()) {
            Long film_id = genresRowSet.getLong("film_id");
            Long genre_id = genresRowSet.getLong("fggenre_id");
            String genre_name = genresRowSet.getString("gname");
            if (genreMap.containsKey(film_id)) {
                genreMap.get(film_id).add(new Genre(genre_id, genre_name));
            } else {
                genreMap.put(film_id, new HashSet<>());
                genreMap.get(film_id).add(new Genre(genre_id, genre_name));
            }
        }
        return genreMap;
    }


    @Override
    public Film getFilm(Long id) {
        String sql = "select  film_id, f.name as fname, description, releaseDate, duration, " +
                "f.rating_id as frating_id, rm.name as rmname " +
                "from films as f " +
                "join rating_mpa as rm on f.rating_id = rm.rating_id " +
                "where film_id = ?";
        String sqlGenres = "select film_id, fg.GENRE_ID as fggenre_id, g.name as gname " +
                "from film_genres as fg join genres as g on fg. genre_id = g. genre_id " +
                "where film_id = ?";

        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(sql, id);
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(sqlGenres, id);

        Map<Long, Set<Genre>> genreMap = makeGenreMap(genresRowSet);

        if (filmRowSet.next()) {
            Film film = new Film(
                    filmRowSet.getLong("film_id"),
                    filmRowSet.getString("fname"),
                    filmRowSet.getString("description"),
                    filmRowSet.getDate("releaseDate").toLocalDate(),
                    filmRowSet.getInt("duration"),
                    new RatingMPA(filmRowSet.getLong("frating_id"), filmRowSet.getString("rmname")),
                    genreMap.getOrDefault(filmRowSet.getLong("film_id"), null)
            );

            return film;
        } else {
            return null;
        }
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Long id = simpleJdbcInsert.executeAndReturnKey(toMap(film)).longValue();

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)",
                        id,
                        genre.getId());
            }
        }

        return getFilm(id);
    }

    @Override
    public Film update(Film film) {
        //обновляет таблицу films
        String sql = "update films set name = ?, description = ?, releasedate = ?, duration = ?, rating_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        //обновляет таблицу genres
        Set<Long> newGenres = new HashSet<>();
        if (film.getGenres() != null)
            newGenres = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());

        Set<Long> oldGenres = new HashSet<>();
        String sqlGenres = "select * from film_genres where film_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenres, film.getId());
        while (genreRows.next()){
            oldGenres.add(genreRows.getLong("genre_id"));
        }

        Set<Long> clone = new HashSet<>(oldGenres);
        oldGenres.removeAll(newGenres);
        newGenres.removeAll(clone);

        for (Long genreId: oldGenres) {
            jdbcTemplate.update("delete from film_genres where film_id = ? and genre_id = ?", film.getId(), genreId);
        }

        for (Long genreId: newGenres) {
            jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)", film.getId(), genreId);
        }


        Film filmFromDb = getFilm(film.getId());

        //костыль для тестов: при запросе get фильма без жанров требуется film.genres = null, при update film.genres.length = 0
        if (film.getGenres() != null) {
            if (film.getGenres().isEmpty()) {
                return new Film(filmFromDb.getId(),
                        filmFromDb.getName(),
                        filmFromDb.getDescription(),
                        filmFromDb.getReleaseDate(),
                        filmFromDb.getDuration(),
                        filmFromDb.getMpa(),
                        new HashSet<Genre>());
            }
        }
        return filmFromDb;
    }

    /** Для работы метода SimpleJdbcInsert.executeAndReturnKey
     * @param film
     * @return
     */
    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("releaseDate", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        values.put("likes_counter", 0);
        return values;
    }

    @Override
    public RatingMPA getMpa(Long ratingId) {
        String sql = "select * from rating_MPA where rating_id = ?";

        SqlRowSet mpaRowSet = jdbcTemplate.queryForRowSet(sql, ratingId);

        if (mpaRowSet.next()) {
            RatingMPA ratingMPA = new RatingMPA(mpaRowSet.getLong("rating_id"),
                                                mpaRowSet.getString("name"));
            return ratingMPA;
        } else {
            return null;
        }
    }

    @Override
    public List<RatingMPA> getAllMpa() {
        String sql = "select * from rating_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRatingMPA(rs));
    }

    /**
     * Маппер
     */
    private RatingMPA makeRatingMPA(ResultSet rs) throws SQLException {
        Long id = rs.getLong("rating_id");
        String name = rs.getString("name");
        return new RatingMPA(id, name);
    }

    @Override
    public Genre getGenre(Long genreId) {
        String sql = "select * from genres where genre_id = ?";

        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sql, genreId);

        if (genreRowSet.next()) {
            return new Genre(genreRowSet.getLong("genre_id"),
                                    genreRowSet.getString("name"));
        } else {
            return null;
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    /**
     * Маппер
     */
    private Genre makeGenre(ResultSet rs) throws SQLException {
        Long id = rs.getLong("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
