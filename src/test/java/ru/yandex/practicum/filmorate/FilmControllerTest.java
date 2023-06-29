package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmControllerTest {
    private final FilmController controller = new FilmController();
    private final Film film = Film.builder()
            .id(1)
            .name("Movie")
            .description("The most awesome movie I've ever seen")
            .releaseDate(LocalDate.of(2020, 2, 2))
            .duration(120)
            .build();

    @Test
    void create_shouldAddAMovie() {
        Film thisFilm = new Film(1, "Movie", "The most awesome movie I've ever seen",
                LocalDate.of(2020, 2, 2), 120);
        controller.create(thisFilm);

        Assertions.assertEquals(film, thisFilm);
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void update_shouldUpdateMovieData() {
        Film thisFilm = new Film(1, "Movie", "I cried at the end, it was very thoughtful",
                LocalDate.of(2020, 2, 2), 120);
        controller.create(film);
        controller.update(thisFilm);

        Assertions.assertEquals("I cried at the end, it was very thoughtful", thisFilm.getDescription());
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithAnEmptyName() {
        film.setName("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithDescriptionMoreThan200() {
        film.setDescription("This is the most amazing and terrifying movie in my life. I love scary movies," +
                "but I've never seen such precise details of serial killers doing thier job." +
                "You should deffinately see this one. Actually, this movie was based on a true story. Creepy...");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieWithDateReleaseMoreThan1895() {
        film.setReleaseDate(LocalDate.of(1891, 2, 2));

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void create_shouldNotAddAMovieIfDurationIsMoreThan0() {
        film.setDuration(-15);

        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }
}
