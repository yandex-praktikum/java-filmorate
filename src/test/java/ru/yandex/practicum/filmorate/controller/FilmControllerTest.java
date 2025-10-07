package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private Film film;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
        film = new Film();
        film.setName("Inception");
        film.setDescription("Mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
    }

    @Test
    void shouldAddValidFilm() {
        Film added = controller.addFilm(film);
        assertEquals(1, added.getId());
        assertEquals("Inception", added.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        film.setName("");
        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionTooLong() {
        film.setDescription("A".repeat(201));
        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldThrowExceptionWhenReleaseBefore1895() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }
}
