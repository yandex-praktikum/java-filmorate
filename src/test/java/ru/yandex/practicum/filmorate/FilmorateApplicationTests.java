package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Locale;

@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;

    @Test
    void contextLoads() {
    }

    @Test
    void testUsers() {
        User user = userService.create(User
                .builder()
                .name("Steve")
                .email("steve@gmail.com")
                .login("steve")
                .birthday(LocalDate.of(1990, 12, 5))
                .build());

        assert (userService.findAll().get(0).equals(user));
    }

    @Test
    void testFilms() {
        Film film = filmService.create(Film
                .builder()
                .name("Film")
                .description("Info")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(123.12F)
                .build());

        assert (filmService.findAll().get(0).equals(film));
    }
}
