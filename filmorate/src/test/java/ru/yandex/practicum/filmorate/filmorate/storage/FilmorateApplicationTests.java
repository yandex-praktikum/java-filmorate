package ru.yandex.practicum.filmorate.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.filmorate.model.Film;
import ru.yandex.practicum.filmorate.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        String sqlInsert = "INSERT INTO USER (USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY)" +
                "VALUES (1, 'Dima', 'dzr', 'dz@yandex.ru', '1977-6-17')";

        jdbcTemplate.update(sqlInsert);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.create(new User(
                2,
                "common",
                "@mail.ru",
                "friend",
                LocalDate.of(1976, 8, 20),
                null,
                null
                )
        ));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testUpdateUser() {
        String sqlInsert = "INSERT INTO USER (USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY)" +
                "VALUES (1, 'Dima', 'dzr', 'dz@yandex.ru', '1977-6-17')";

        jdbcTemplate.update(sqlInsert);
        Optional<User> userOptional = Optional.ofNullable(userStorage.update(new User(
                1,
                "update",
                "@mail.ru",
                "friend",
                LocalDate.of(1976, 8, 20),
                null,
                null
        )));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }


    @Test
    public void testFindFilmById() {
        String sqlInsert = "INSERT INTO FILM (FILM_ID, FILM_NAME, DESCRIPTION, release_date, duration, rating_id)" +
                "VALUES (1, 'SW', 'pp', '1977-6-17', 20, 1)";

        jdbcTemplate.update(sqlInsert);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(new Film(
                        2,
                        "common",
                        "@mail.ru",
                        LocalDate.of(1976, 8, 20),
                        10,
                        null,
                        null
                )));
        AssertionsForClassTypes.assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testUpdateFilm() {
        String sqlInsert = "INSERT INTO FILM (FILM_ID, FILM_NAME, DESCRIPTION, release_date, duration, rating_id)" +
                "VALUES (1, 'SW', 'pp', '1977-6-17', 20, 1)";

        jdbcTemplate.update(sqlInsert);
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.update(new Film(
                1,
                "update",
                "@mail.ru",
                LocalDate.of(1976, 8, 20),
                10,
                null,
                null
        )));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}