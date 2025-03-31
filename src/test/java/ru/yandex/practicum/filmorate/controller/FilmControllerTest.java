package ru.yandex.practicum.filmorate.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Map<Integer, Film> films;

    @BeforeEach
    public void setUp() {
        films = new HashMap<>();
        filmController = new FilmController();
    }

    @Test
    void findAll() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("Description of Film 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Film film2 = new Film();
        film2.setName("Film 1");
        film2.setDescription("Description of Film 2");
        film2.setReleaseDate(LocalDate.of(2002, 5, 7));
        film2.setDuration(144);
        Film addedFilm = filmController.addNewFilm(film);
        Film addedFilm2 = filmController.addNewFilm(film2);
        Collection<Film> films = filmController.findAll();
        Collection<Film> filmsExp = List.of(film, film2);
        Assertions.assertThat(films).containsExactlyElementsOf(filmsExp);
    }

    @Test
    void addNewFilm() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("Description of Film 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Film addedFilm = filmController.addNewFilm(film);
        assertNotNull(addedFilm);
        assertEquals("Film 1", addedFilm.getName());
        assertEquals("Description of Film 1", addedFilm.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), addedFilm.getReleaseDate());
        assertEquals(120, addedFilm.getDuration());
    }

    @Test
    void updateFilm() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("Description of Film 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Film addedFilm = filmController.addNewFilm(film);
        assertNotNull(addedFilm);
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("Film 1");
        newFilm.setDescription("Description of Film 1 EDITED");
        newFilm.setReleaseDate(LocalDate.of(2003, 2, 5));
        newFilm.setDuration(134);
        Film updatedFilm = filmController.updateFilm(newFilm);
        assertNotNull(updatedFilm);
        assertEquals("Film 1", updatedFilm.getName());
        assertEquals("Description of Film 1 EDITED", updatedFilm.getDescription());
        assertEquals(LocalDate.of(2003, 2, 5), updatedFilm.getReleaseDate());
        assertEquals(134, updatedFilm.getDuration());
    }

    @Test
    void addNewFilmWithErrors() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description of Film 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        ValidationException thrown1 = assertThrows(
                ValidationException.class,
                () -> filmController.addNewFilm(film),
                "Ожидалось исключение ValidationException для пустого имени"
        );
        assertEquals("Название фильма не должно быть пустым", thrown1.getMessage());
        Film film2 = new Film();
        film2.setName("Film");
        film2.setDescription("A".repeat(201));
        film2.setReleaseDate(LocalDate.of(1895, 12, 28));
        film2.setDuration(120);
        ValidationException thrown2 = assertThrows(
                ValidationException.class,
                () -> filmController.addNewFilm(film2),
                "Ожидалось исключение ValidationException для длины описания"
        );
        assertEquals("Максимальная длина описания фильма — 200 символов", thrown2.getMessage());
        Film film3 = new Film();
        film3.setName("Film 1");
        film3.setDescription("Description of Film 1");
        film3.setReleaseDate(LocalDate.of(1895, 12, 27));
        film3.setDuration(120);
        ValidationException thrown3 = assertThrows(
                ValidationException.class,
                () -> filmController.addNewFilm(film3),
                "Ожидалось исключение ValidationException для неверной даты релиза"
        );
        assertEquals("Дата релиза фильма должна быть не раньше 1895 12 28 года", thrown3.getMessage());
        Film film4 = new Film();
        film4.setName("Film 1");
        film4.setDescription("Description of Film 1");
        film4.setReleaseDate(LocalDate.of(2000, 1, 1));
        film4.setDuration(-120);
        ValidationException thrown4 = assertThrows(
                ValidationException.class,
                () -> filmController.addNewFilm(film4),
                "Ожидалось исключение ValidationException для отрицательной длительности"
        );
        assertEquals("Продолжительность фильма должна быть положительным числом", thrown4.getMessage());
    }

    @Test
    void updateFilmWithErrors() {
        Film film = new Film();
        film.setName("Film 1");
        film.setDescription("Description of Film 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        Film addedFilm = filmController.addNewFilm(film);
        Film newFilm = new Film();
        newFilm.setName("Film 1");
        newFilm.setDescription("Description of Film 1 EDITED");
        newFilm.setReleaseDate(LocalDate.of(2003, 2, 5));
        newFilm.setDuration(134);
        ValidationException thrown1 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(newFilm),
                "Ожидалось исключение ValidationException для пустого ID"
        );
        assertEquals("Id фильма должен быть указан", thrown1.getMessage());
        Film newFilm2 = new Film();
        newFilm2.setId(2);
        newFilm2.setName("Film 1");
        newFilm2.setDescription("Description of Film 1 EDITED");
        newFilm2.setReleaseDate(LocalDate.of(2003, 2, 5));
        newFilm2.setDuration(134);
        NotFoundException thrown2 = assertThrows(
                NotFoundException.class,
                () -> filmController.updateFilm(newFilm2),
                "Ожидалось исключение NotFoundException для ненайденного фильма"
        );
        assertEquals("Фильм с id = " + newFilm2.getId() + " не найден", thrown2.getMessage());
        Film newFilm3 = new Film();
        newFilm3.setId(1);
        newFilm3.setName("");
        newFilm3.setDescription("Description of Film 1 EDITED");
        newFilm3.setReleaseDate(LocalDate.of(2003, 2, 5));
        newFilm3.setDuration(134);
        ValidationException thrown3 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(newFilm3),
                "Ожидалось исключение ValidationException для пустого имени"
        );
        assertEquals("Название фильма не должно быть пустым", thrown3.getMessage());
        Film newFilm4 = new Film();
        newFilm4.setId(1);
        newFilm4.setName("Film 1");
        newFilm4.setDescription("A".repeat(201));
        newFilm4.setReleaseDate(LocalDate.of(2003, 2, 5));
        newFilm4.setDuration(134);
        ValidationException thrown4 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(newFilm4),
                "Ожидалось исключение ValidationException для длины описания"
        );
        assertEquals("Максимальная длина описания фильма — 200 символов", thrown4.getMessage());
        Film newFilm5 = new Film();
        newFilm5.setId(1);
        newFilm5.setName("Film 1");
        newFilm5.setDescription("Description of Film 1");
        newFilm5.setReleaseDate(LocalDate.of(1895, 12, 27));
        newFilm5.setDuration(134);
        ValidationException thrown5 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(newFilm5),
                "Ожидалось исключение ValidationException для неверной даты релиза"
        );
        assertEquals("Дата релиза фильма должна быть не раньше 1895 12 28 года", thrown5.getMessage());
        Film newFilm6 = new Film();
        newFilm6.setId(1);
        newFilm6.setName("Film 1");
        newFilm6.setDescription("Description of Film 1");
        newFilm6.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm6.setDuration(-120);
        ValidationException thrown6 = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(newFilm6),
                "Ожидалось исключение ValidationException для отрицательной длительности"
        );
        assertEquals("Продолжительность фильма должна быть положительным числом", thrown6.getMessage());
    }
}