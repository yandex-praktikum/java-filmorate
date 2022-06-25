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
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmStorage filmStorage;
    TestUtil testUtil = new TestUtil(new JdbcTemplate());

    @Test
    public void testFindUserById() {
        testUtil.clean();
        testUtil.addUser();
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.create(new User(
                "common",
                "@mail.ru",
                "friend",
                LocalDate.of(1976, 8, 20)
        )));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testUpdateUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.update(new User(
                2,
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
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }
}