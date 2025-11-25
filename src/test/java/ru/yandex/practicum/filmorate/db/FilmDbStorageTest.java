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
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("dbFilms")
@Import({FilmDbStorage.class, GenreDbStorage.class, MpaDbStorage.class})
@Sql(scripts = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest implements FilmorateTestSupport {

    @Autowired
    private FilmDbStorage filmStorage;

    private Film testFilm;

    @BeforeEach
    void setUp() {
        testFilm = filmStorage.create(
                film("Test Film", "Description of test film", LocalDate.of(2020, 1, 1), 120)
        );
        assertThat(testFilm.getId()).isPositive();
    }

    @Test
    void create_shouldPersistAndReturnId() {
        Film created = filmStorage.create(
                film("New Film", "New description", LocalDate.of(2021, 5, 20), 90)
        );

        assertThat(created).isNotNull();
        assertThat(created.getId()).isPositive();

        Optional<Film> reloaded = filmStorage.findById(created.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getName()).isEqualTo("New Film");
        assertThat(reloaded.get().getDuration()).isEqualTo(90);
    }

    @Test
    void findById_whenExists_returnsFilm() {
        Optional<Film> found = filmStorage.findById(testFilm.getId());
        assertThat(found).isPresent()
                .get()
                .satisfies(f -> {
                    assertThat(f.getId()).isEqualTo(testFilm.getId());
                    assertThat(f.getName()).isEqualTo("Test Film");
                });
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        assertThat(filmStorage.findById(999_999L)).isEmpty();
    }

    @Test
    void update_whenValid_updatesRow() {
        testFilm.setName("Updated Film Name");
        testFilm.setDescription("Updated description");

        Film updated = filmStorage.update(testFilm);

        assertThat(updated.getName()).isEqualTo("Updated Film Name");
        assertThat(updated.getDescription()).isEqualTo("Updated description");

        // перепрочитаем из БД
        Film reloaded = filmStorage.findById(testFilm.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Updated Film Name");
        assertThat(reloaded.getDescription()).isEqualTo("Updated description");
        assertThat(reloaded.getDuration()).isEqualTo(120);
    }

    @Test
    void deleteById_whenExists_removesRow() {
        assertThat(filmStorage.findById(testFilm.getId())).isPresent();

        filmStorage.deleteById(testFilm.getId());

        assertThat(filmStorage.findById(testFilm.getId())).isEmpty();
    }
}
