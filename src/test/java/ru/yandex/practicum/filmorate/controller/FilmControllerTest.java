package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;
    private Film validFilm;
    private BindingResult bindingResult;

    @BeforeEach
    public void setup() {
        openMocks(this);
        validFilm = new Film();
        validFilm.setName("Inception");
        validFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        bindingResult = new BeanPropertyBindingResult(validFilm, "film");
    }

    @Test
    public void whenAddFilmWithInvalidReleaseDate_thenInternalServerError() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27)); // Invalid date before cinema inception

        ResponseEntity<Object> response = filmController.addFilm(validFilm, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Error creating film due to invalid input"));
    }

    @Test
    public void whenUpdateFilmWithInvalidReleaseDate_thenInternalServerError() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27)); // Invalid release date

        ResponseEntity<Object> response = filmController.updateFilm(1L, validFilm, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error").toString().contains("Error updating film due to invalid input"));
    }

    @Test
    public void whenUpdateFilmNotFound_thenNotFound() {
        when(filmService.updateFilm(anyLong(), any(Film.class))).thenReturn(null);

        ResponseEntity<Object> response = filmController.updateFilm(1L, validFilm, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error").toString().contains("Film not found with ID"));
    }


    @Test
    public void whenUpdateFilmWithPastReleaseDate_thenThrowValidationException() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27)); // Invalid date before the first film release

        ResponseEntity<Object> response = filmController.updateFilm(1L, validFilm, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Error updating film due to invalid input"));
    }

    @Test
    public void whenUpdateFilmWithBindingErrors_thenBadRequest() {
        bindingResult.rejectValue("name", "Error", "Name cannot be empty");

        ResponseEntity<Object> response = filmController.updateFilm(1L, validFilm, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Validation error"));
    }

    @Test
    public void whenAddFilmWithBindingErrors_thenBadRequest() {
        bindingResult.rejectValue("name", "Error", "Name cannot be empty");

        ResponseEntity<Object> response = filmController.addFilm(validFilm, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Validation error"));
    }

    @Test
    public void whenAddFilmWithValidData_thenCreateFilm() {
        Film film = new Film(1L, "Interstellar", "desc",
                LocalDate.of(2014, 11, 7), 169);
        when(filmService.addFilm(any(Film.class))).thenReturn(film);

        BindingResult bindingResult = new BeanPropertyBindingResult(film, "film");
        ResponseEntity<Object> response = filmController.addFilm(film, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(filmService).addFilm(any(Film.class));
    }

    @Test
    public void whenAddFilmWithInvalidDate_thenBadRequest() {
        Film film = new Film(1L, "A Trip to the Moon", "Desc",
                LocalDate.of(1895, 12, 27), 15);
        BindingResult bindingResult = new BeanPropertyBindingResult(film, "film");

        ResponseEntity<Object> response = filmController.addFilm(film, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).containsKey("error"));
    }

    @Test
    public void whenUpdateFilmWithValidData_thenUpdateFilm() {
        Film film = new Film(1L, "Blade Runner", "Neo-noir", LocalDate.of(1982, 6, 25), 117);
        when(filmService.updateFilm(eq(1L), any(Film.class))).thenReturn(film);

        BindingResult bindingResult = new BeanPropertyBindingResult(film, "film");
        ResponseEntity<Object> response = filmController.updateFilm(1L, film, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(filmService).updateFilm(eq(1L), any(Film.class));
    }

    @Test
    public void whenUpdateFilmNotFound_thenNotFoundResponse() {
        Film film = new Film(1L, "Fargo", "Crime thriller",
                LocalDate.of(1996, 3, 8), 98);
        when(filmService.updateFilm(eq(2L), any(Film.class))).thenReturn(null);

        BindingResult bindingResult = new BeanPropertyBindingResult(film, "film");
        ResponseEntity<Object> response = filmController.updateFilm(2L, film, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenGetAllFilms_thenAllFilmsReturned() {
        List<Film> films = List.of(new Film(1L, "Inception", "Thriller",
                LocalDate.of(2010, 7, 16), 148));
        when(filmService.getAllFilms()).thenReturn(films);

        ResponseEntity<List<Film>> response = filmController.getAllFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(filmService).getAllFilms();
    }
}
