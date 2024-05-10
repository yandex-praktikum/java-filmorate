package ru.yandex.practicum.filmorate.service.film;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.impl.FilmServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class FilmServiceImplTest {

    private FilmServiceImpl filmService;

    @BeforeEach
    public void setup() {
        filmService = new FilmServiceImpl();
    }

    @Test
    public void whenAddFilm_thenFilmIsAddedWithNewId() {
        Film film = new Film(1L, "Inception", "desc",
                LocalDate.of(2010, 7, 16), 148);
        Film result = filmService.addFilm(film);

        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals("Inception", result.getName());
        assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    public void whenUpdateExistingFilm_thenFilmIsUpdated() {
        Film film = new Film(1L, "The Matrix", "desc",
                LocalDate.of(1999, 3, 31), 136);
        Film createdFilm = filmService.addFilm(film);
        createdFilm.setDescription("Updated description");

        Film updatedFilm = filmService.updateFilm(createdFilm.getId(), createdFilm);

        assertNotNull(updatedFilm);
        assertEquals("Updated description", updatedFilm.getDescription());
        assertEquals(createdFilm.getId(), updatedFilm.getId());
    }

    @Test
    public void whenUpdateNonExistingFilm_thenNoFilmIsUpdated() {
        Film film = new Film(1L, "Avatar", "desc",
                LocalDate.of(2009, 12, 18), 162);
        Film updatedFilm = filmService.updateFilm(999L, film);

        assertNull(updatedFilm);
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    public void whenGetAllFilms_thenAllFilmsAreReturned() {
        filmService.addFilm(new Film(1L, "Fight Club", "desc1",
                LocalDate.of(1999, 10, 15), 139));
        filmService.addFilm(new Film(2L, "Pulp Fiction", "desc2",
                LocalDate.of(1994, 10, 14), 154));

        List<Film> films = filmService.getAllFilms();

        assertEquals(2, films.size());
        assertTrue(films.stream().anyMatch(film -> film.getName().equals("Fight Club")));
        assertTrue(films.stream().anyMatch(film -> film.getName().equals("Pulp Fiction")));
    }
}