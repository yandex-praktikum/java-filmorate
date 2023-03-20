package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class FilmrControllerTest {

    private FilmController filmController;
    Film film;

    @BeforeEach
    public void createController() {
        filmController = new FilmController();
        film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
    }

    @Test
    public void shouldCreateUser() {

        filmController.addFilm(film);
        List<Film> allFIlmss = filmController.getAllFIlms();

        assertEquals(1, allFIlmss.size(), "Size Equal Test");

        Film filmToCheck = allFIlmss.get(0);

        assertEquals(film.getName(), filmToCheck.getName(), "Name Equal Test");
        assertEquals(film.getDescription(), filmToCheck.getDescription(), "Description Equal Test");

        assertEquals(film.getReleaseDate(), filmToCheck.getReleaseDate(), "ReleaseDate Equal Test");
        assertEquals(film.getDuration(), filmToCheck.getDuration(), "Duration Equal Test");
    }

    @Test
    public void shouldCreateUserWithoutName() {
        film.setName("");
        filmController.addFilm(film);

        List<Film> allUsers = filmController.getAllFIlms();

        assertEquals(1, allUsers.size(),
                "Size Equal Test");

    }


    @Test
    public void shouldThrowExceptionWhenUpdateUnknownUser() {
        film.setId(1);

        NotFoundException e = assertThrows(
                NotFoundException.class, () -> filmController.updateFilm(film));

        assertEquals("404 NOT_FOUND \"Film don't find\"", e.getMessage());
        assertEquals(0,
                filmController.getAllFIlms().size(),
                "Size Equal Test");
    }

    @Test
    public void shouldGetAllUserTest()  {
        filmController.addFilm(film);
        Film film100 = film;
        film100.setId(100);

        filmController.addFilm(film100);

        assertEquals(2, filmController.getAllFIlms().size(),
                "Size Equal Test");
    }
}