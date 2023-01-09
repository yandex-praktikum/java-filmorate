package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final DbUserStorage userStorage;

    @Test
    public void testFindUserById() {
        User user1 = new User(1L, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));
        userStorage.createUser(user1);
        Optional<User> userOptional = userStorage.getById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));
        User user2 = new User(2L, "user2@example", "User2"
                , "user2", LocalDate.of(2000, 2, 1));
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        List<User> users = userStorage.getAll();
        assertThat(users.size())
                .isEqualTo(2);
    }
}
