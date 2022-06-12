package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {

    FilmIdGenerator filmIdGenerator = new FilmIdGenerator();
    FilmController filmController = new FilmController(filmIdGenerator);

    @Test
    void findAll(){
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100);
        filmController.create(film);
        assertEquals(1, filmController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create(){
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100);
        Film testFilm = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100);
        filmController.create(film);
        testFilm.setId(film.getId());
        assertEquals(testFilm, filmController.getFilm(film.getId()), "Фильм не добавлен");
    }

    @Test
    void saveFilm(){
        Film film1 = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 03, 25), 100);
        Film film2 = new Film("terminator", "adipisicing", LocalDate.of(1967, 03, 25), 777);
        Film testFilm = new Film("terminator", "adipisicing", LocalDate.of(1967, 03, 25), 777);
        filmController.create(film1);
        film2.setId(film1.getId());
        filmController.saveFilm(film2);
        testFilm.setId(film1.getId());
        assertEquals(testFilm, filmController.getFilm(film2.getId()), "Фильм не добавлен");
    }
}
