package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmorateFilmTests {

    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void clearDB() {
        filmController.deleteFilms();
    }

    @Test
    public void filmControllerValidEntityTest() throws ValidateException {
        Film film =
                new Film(1L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                        , 100, 0L);
        film.setMpa(new Mpa(1L, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1L, ""));
        film.setGenres(genres);
        filmController.createFilm(film);

        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - добавлен 1 фильм");
    }

    @Test
    public void filmControllerInvalidNameTest() {
        Film film = new Film(1L, "", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        film.setName(null);
        assertThrows(NullPointerException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidLengthDescriptionTest() {
        String s = "";
        for (int i = 0; i < 41; i++) {
            s += "desc ";
        }
        Film film = new Film(1L, "Example", s, LocalDate.of(2000, 1, 1)
                , 100, 0L);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDateReleaseTest() {
        Film film = new Film(1L, "Example", "desc Example"
                , LocalDate.of(1895, 12, 27)
                , 100, 0L);
        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDurationTest() {
        Film film = new Film(1L, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1), 100, 0L);
        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        film.setDuration(0);
        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerUpdateEntityTest() {
        Film film = new Film(1L, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1)
                , 100, 0L);
        film.setMpa(new Mpa(1L, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1L, ""));
        film.setGenres(genres);

        Film filmUpd = new Film(4L, "Example2", "desc Example2"
                , LocalDate.of(2002, 1, 1), 100, 0L);
        filmUpd.setMpa(new Mpa(1L, ""));
        filmUpd.setGenres(genres);

        filmController.createFilm(film);
        filmController.updateFilm(filmUpd);
        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - обновлен 1 фильм");
    }

    @Test
    public void filmControllerGetFilmsTest() throws NullPointerException {
        Film film = new Film(1L, "Example", "desc Example",
                LocalDate.of(2000, 1, 1), 100, 0L);

        Film film2 = new Film(2L, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1), 100, 0L);
        film.setMpa(new Mpa(1L, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1L, ""));
        film.setGenres(genres);
        film2.setMpa(new Mpa(1L, ""));
        film2.setGenres(genres);

        filmController.createFilm(film);
        filmController.createFilm(film2);
        List<Film> films = filmController.getFilms();

        Assertions.assertEquals(films.size(), 2, "ожидается - получено 2 фильма");
    }
}
