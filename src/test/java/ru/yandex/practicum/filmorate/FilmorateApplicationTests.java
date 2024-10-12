package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        this.filmController = new FilmController();
    }

    @Test
    public void shouldCreateFilm() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();
        Film filmControllerCreate = filmController.create(film);

        Assertions.assertEquals(film, filmControllerCreate, "Фильмы должны быть одинаковы");
    }

    @Test
    public void shouldUpdateFilm() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();
        filmController.create(film);

        film = Film.builder()
                .id(1L)
                .name("Titanic and Lodka")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();

        Film filmControllerUpdate = filmController.update(film);
        Assertions.assertEquals(film, filmControllerUpdate, "Фильм должен соответствовать обновлённому");
    }

    @Test
    public void shouldGetAllFilms() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();
        filmController.create(film);

        List<Film> listFilms = filmController.allListFilm();

        Assertions.assertEquals(1, listFilms.size(), "Список должен содержать 1 фильм");
    }

    @Test
    public void shouldntNameFilmToBeEmpty() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void shouldMaxLongDescription200Char() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Слишком длинное описание, которое превышает допустимую длину ........................." +
                        "...................." + ".........................................." +
                        ".....................................................")
                .releaseDate(localDate)
                .duration(duration)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void shouldReleaseDateNotBefore28December1985Year() {
        LocalDate localDate = LocalDate.of(1885, 12, 16);
        Duration duration = Duration.ofMinutes(194);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }

    @Test
    public void shouldDurationFilmInPositive() {
        LocalDate localDate = LocalDate.of(1997, 12, 16);
        Duration duration = Duration.ofMinutes(-178);
        Film film = Film.builder()
                .id(1L)
                .name("Titanic")
                .description("Description Long")
                .releaseDate(localDate)
                .duration(duration)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
    }
}
