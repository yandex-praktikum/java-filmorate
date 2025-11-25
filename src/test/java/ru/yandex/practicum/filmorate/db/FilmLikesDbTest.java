package ru.yandex.practicum.filmorate.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("dbFilms")
@Import({FilmDbStorage.class, GenreDbStorage.class, MpaDbStorage.class})
@Sql(scripts = {"/schema.sql", "/testdata-h2.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class FilmLikesDbTest implements FilmorateTestSupport {

    private final long userId = 1L;
    @Autowired
    private FilmDbStorage filmStorage;
    @Autowired
    private JdbcTemplate jdbc;
    private Film a;
    private Film b;

    @BeforeEach
    void setUp() {
        a = filmStorage.create(film("A", "a", LocalDate.of(2020, 1, 1), 100));
        b = filmStorage.create(film("B", "b", LocalDate.of(2020, 1, 2), 110));
    }

    @Test
    void addLike_shouldIncreasePopularityAndBeIdempotent() {
        filmStorage.addLike(a.getId(), userId);
        filmStorage.addLike(a.getId(), userId);

        Integer cnt = jdbc.queryForObject("select count(*) from likes where film_id = ?", Integer.class, a.getId());
        assertThat(cnt).isEqualTo(1);

        List<Film> popular = new ArrayList<>(filmStorage.findPopular(10));
        assertThat(popular).extracting(Film::getId)
                .containsExactly(a.getId(), b.getId());
    }

    @Test
    void removeLike_shouldDecreaseCountAndAffectOrder() {
        filmStorage.addLike(a.getId(), userId);
        filmStorage.addLike(b.getId(), userId);
        filmStorage.removeLike(a.getId(), userId);

        List<Film> popular = new ArrayList<>(filmStorage.findPopular(10));
        assertThat(popular).extracting(Film::getId)
                .containsExactly(b.getId(), a.getId());
    }

    @Test
    void popularTieBreak_shouldOrderByIdAscWhenLikesEqual() {
        Collection<Film> popular = filmStorage.findPopular(2);
        List<Long> ids = popular.stream().map(Film::getId).toList();
        assertThat(ids).containsExactly(a.getId(), b.getId());
    }

    @Test
    void addLike_withUnknownUser_shouldFailWithFkViolation() {
        assertThatThrownBy(() -> filmStorage.addLike(a.getId(), 9999L))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}