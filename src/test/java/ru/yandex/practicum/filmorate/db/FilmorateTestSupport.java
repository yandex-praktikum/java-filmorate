package ru.yandex.practicum.filmorate.db;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;

public interface FilmorateTestSupport {

    default User user(String email, String login, String name, LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    default Film film(String name, String description, LocalDate releaseDate, int duration) {
        return Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
    }
}
