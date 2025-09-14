package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();

        validFilm = new Film();
        validFilm.setName("Test Film");
        validFilm.setDescription("Test description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    void shouldCreateValidFilm() {
        Film createdFilm = filmController.create(validFilm);

        assertNotNull(createdFilm);
        assertEquals("Test Film", createdFilm.getName());
        assertEquals(120, createdFilm.getDuration());
        assertTrue(createdFilm.getId() > 0);
    }

    @Test
    void shouldGetAllFilms() {
        filmController.create(validFilm);
        List<Film> films = filmController.getAllFilms();

        assertEquals(1, films.size());
        assertTrue(films.stream().anyMatch(f -> f.getName().equals("Test Film")));
    }

    @Test
    void shouldUpdateFilm() {
        Film createdFilm = filmController.create(validFilm);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(150);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals("Updated Film", result.getName());
        assertEquals(150, result.getDuration());
        assertEquals(createdFilm.getId(), result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentFilm() {
        Film nonExistentFilm = new Film();
        nonExistentFilm.setId(999);
        nonExistentFilm.setName("Non-existent");
        nonExistentFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        nonExistentFilm.setDuration(100);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.updateFilm(nonExistentFilm));

        assertEquals("Фильм с id 999 не найден", exception.getMessage());
    }

    @Test
    void shouldGenerateUniqueIds() {
        Film film1 = filmController.create(validFilm);

        Film film2 = new Film();
        film2.setName("Second Film");
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
        film2.setDuration(90);
        Film createdFilm2 = filmController.create(film2);

        assertNotEquals(film1.getId(), createdFilm2.getId());
        assertEquals(2, filmController.getAllFilms().size());
    }
}
