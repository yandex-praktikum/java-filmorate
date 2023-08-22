package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FilmControllerTest {

    @BeforeEach
    public void startServer() {
        SpringApplication.run(FilmorateApplication.class);
    }

    @Test
    public void createFilmTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967,3,25))
                .duration(100)
                .build();

        HttpServletRequest request = mock(HttpServletRequest.class);

        FilmController controller = new FilmController();
        ResponseEntity<?> entity = controller.createFilm(film, request);
        assertEquals("200 OK", entity.getStatusCode().toString());

        film.setName(" ");
        assertThrows((ValidationException.class),
                () -> controller.createFilm(film, request));
        film.setName("nisi eiusmod");

        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        assertThrows((ValidationException.class),
                () -> controller.createFilm(film, request));
        film.setDescription("adipisicing");

        film.setReleaseDate(LocalDate.of(967,3,25));
        assertThrows((ValidationException.class),
                () -> controller.createFilm(film, request));
        film.setReleaseDate(LocalDate.of(1967,3,25));

        film.setDuration(-200);
        assertThrows((ValidationException.class),
                () -> controller.createFilm(film, request));
        film.setDuration(100);
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967,3,25))
                .duration(100)
                .build();

        HttpServletRequest request = mock(HttpServletRequest.class);

        FilmController controller = new FilmController();
        ResponseEntity<?> entity = controller.createFilm(film, request);
        assertEquals("200 OK", entity.getStatusCode().toString());
        System.out.println(entity.getBody());

        film.setName("Film Updated");
        film.setDescription("New film update decription");
        film.setDuration(150);

        entity = controller.updateFilm(film, request);
        assertEquals("200 OK", entity.getStatusCode().toString());

        film.setName(" ");
        assertThrows((ValidationException.class),
                () -> controller.updateFilm(film, request));
        film.setName("nisi eiusmod");

        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        assertThrows((ValidationException.class),
                () -> controller.updateFilm(film, request));
        film.setDescription("adipisicing");

        film.setReleaseDate(LocalDate.of(967,3,25));
        assertThrows((ValidationException.class),
                () -> controller.updateFilm(film, request));
        film.setReleaseDate(LocalDate.of(1967,3,25));

        film.setDuration(-200);
        assertThrows((ValidationException.class),
                () -> controller.updateFilm(film, request));
        film.setDuration(100);

        film.setId(200);
        assertThrows((ValidationException.class),
                () -> controller.updateFilm(film, request));
        film.setId(1);
    }
}