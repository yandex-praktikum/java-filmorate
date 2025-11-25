package ru.yandex.practicum.filmorate.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("dbFilms")
@Import({FilmDbStorage.class, GenreDbStorage.class, MpaDbStorage.class})
@Sql(scripts = {"classpath:schema.sql", "classpath:testdata-h2.sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class FilmGenresDbTest {

    @Autowired
    private FilmDbStorage films;

    private Film film;

    private static Genre g(int id) {
        return Genre.builder().id(id).build();
    }

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .name("With genres")
                .description("desc")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .genres(new LinkedHashSet<>(List.of(g(5), g(2), g(2), g(1))))
                .build();
        film = films.create(film);
    }

    @Test
    void create_shouldDedupAndSortGenresById() {
        Film reloaded = films.findById(film.getId()).orElseThrow();
        assertThat(reloaded.getGenres()).extracting(Genre::getId)
                .containsExactly(1, 2, 5);
    }

    @Test
    void update_shouldReplaceGenres() {
        Set<Genre> newSet = new LinkedHashSet<>(List.of(g(3), g(6)));
        film.setGenres(newSet);

        Film updated = films.update(film);
        assertThat(updated.getGenres()).extracting(Genre::getId)
                .containsExactly(3, 6);

        Film reloaded = films.findById(film.getId()).orElseThrow();
        assertThat(reloaded.getGenres()).extracting(Genre::getId)
                .containsExactly(3, 6);
    }

    @Test
    void update_withEmptySet_shouldClearLinks() {
        film.setGenres(new LinkedHashSet<>());
        films.update(film);

        Film reloaded = films.findById(film.getId()).orElseThrow();
        assertThat(reloaded.getGenres()).isEmpty();
    }
}
