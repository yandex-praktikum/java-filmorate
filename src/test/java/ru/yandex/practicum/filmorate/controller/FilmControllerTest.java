package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    private final FilmController controller = new FilmController();

    @Test
    void shouldRejectBlankName() {
        Film film = validFilm();
        film.setName(" ");

        assertThrows(ConditionsNotMetException.class, () -> controller.create(film));
    }

    @Test
    void shouldAcceptDescriptionOf200Characters() {
        Film film = validFilm();
        film.setDescription("a".repeat(200));

        Film createdFilm = controller.create(film);

        assertEquals(200, createdFilm.getDescription().length());
    }

    @Test
    void shouldRejectDescriptionLongerThan200Characters() {
        Film film = validFilm();
        film.setDescription("a".repeat(201));

        assertThrows(ConditionsNotMetException.class, () -> controller.create(film));
    }

    @Test
    void shouldAcceptCinemaBirthDate() {
        Film film = validFilm();
        film.setReleaseDate(Instant.parse("1895-12-28T00:00:00Z"));

        Film createdFilm = controller.create(film);

        assertEquals(Instant.parse("1895-12-28T00:00:00Z"), createdFilm.getReleaseDate());
    }

    @Test
    void shouldRejectReleaseDateBeforeCinemaBirthDate() {
        Film film = validFilm();
        film.setReleaseDate(Instant.parse("1895-12-27T23:59:59Z"));

        assertThrows(ConditionsNotMetException.class, () -> controller.create(film));
    }

    @Test
    void shouldRejectZeroDuration() {
        Film film = validFilm();
        film.setDuration(Duration.ZERO);

        assertThrows(ConditionsNotMetException.class, () -> controller.create(film));
    }

    @Test
    void shouldRejectNegativeDuration() {
        Film film = validFilm();
        film.setDuration(Duration.ofMinutes(-1));

        assertThrows(ConditionsNotMetException.class, () -> controller.create(film));
    }

    @Test
    void shouldRejectUpdateForUnknownFilm() {
        Film film = validFilm();
        film.setId(1L);

        assertThrows(NotFoundException.class, () -> controller.update(film));
    }

    private Film validFilm() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(Instant.parse("2000-01-01T00:00:00Z"));
        film.setDuration(Duration.ofMinutes(120));
        return film;
    }
}
