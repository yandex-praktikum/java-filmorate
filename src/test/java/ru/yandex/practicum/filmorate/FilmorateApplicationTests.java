package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

    private final FilmController filmController = new FilmController();
    private final UserController userController = new UserController();

    @Test
    void shouldCreateFilm() {
        Film film = Film.builder().name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        filmController.create(film);
        assertEquals(1, filmController.getFilmService().getFilms().size(), "Film not created");
    }

    @Test
    void shouldEmptyNameValidateException() {
        Film film = Film.builder().name("")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("The name is empty", exception.getMessage());
    }

    @Test
    void shouldDescriptionLengthValidateException() {
        Film film = Film.builder().name("Some film")
                .description("A popular form of mass media, film is a remarkably effective " +
                        "medium for conveying drama and evoking emotion. The art of motion " +
                        "pictures is exceedingly complex, requiring contributions from " +
                        "nearly all the other arts as well as countless technical skills " +
                        "(for example, in sound recording, photography, and optics). Emerging " +
                        "at the end of the 19th century, this new art form became one of the most " +
                        "popular and influential media of the 20th century and beyond.")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("The description length more than 200 symbols", exception.getMessage());
    }

    @Test
    void shouldReleaseDateValidateException() {
        Film film = Film.builder().name("Some film")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1894, 3, 25))
                .duration(100)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Release date earlier than 28-12-1895", exception.getMessage());
    }

    @Test
    void shouldDurationValidateException() {
        Film film = Film.builder().name("Some film")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1994, 3, 25))
                .duration(-100)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Film duration is negative", exception.getMessage());
    }

    @Test
    void shouldUserEmailValidateException() {
        User user = User.builder()
                .name("Name")
                .email("mailyandex.ru")
                .login("Login111")
                .birthday(LocalDate.of(1988, 2, 22))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("E-mail is empty or not contains symbol \"@\"", exception.getMessage());
    }

    @Test
    void shouldUserLoginValidateException() {
        User user = User.builder()
                .name("Name")
                .email("mail@yandex.ru")
                .login("Login 111")
                .birthday(LocalDate.of(1988, 2, 22))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("Login is empty or contains a space", exception.getMessage());
    }

    @Test
    void shouldUserBirthdayValidateException() {
        User user = User.builder()
                .name("Name")
                .email("mail@yandex.ru")
                .login("Login111")
                .birthday(LocalDate.of(2200, 2, 22))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("Date of birth cannot be in the future", exception.getMessage());
    }
}
