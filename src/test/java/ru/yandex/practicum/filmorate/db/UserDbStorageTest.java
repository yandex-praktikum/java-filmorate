package ru.yandex.practicum.filmorate.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(UserDbStorage.class)
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class UserDbStorageTest implements FilmorateTestSupport {

    @Autowired
    private UserDbStorage userStorage;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userStorage.create(
                user("test@example.com", "testlogin", "Test User", LocalDate.of(1990, 1, 1))
        );
        assertThat(testUser.getId()).isPositive();
    }

    @Test
    void shouldCreateUserWithValidData_returnUserWithId() {
        User created = userStorage.create(
                user("new@test.com", "newlogin", "New User", LocalDate.now())
        );

        assertThat(created).isNotNull();
        assertThat(created.getId()).isPositive();
        assertThat(created.getEmail()).isEqualTo("new@test.com");
        assertThat(created.getLogin()).isEqualTo("newlogin");

        // перепроверим, что запись действительно в БД
        Optional<User> reloaded = userStorage.findById(created.getId());
        assertThat(reloaded).isPresent();
    }

    @Test
    void shouldFindUserById_whenUserExists_returnUserOptional() {
        Optional<User> found = userStorage.findById(testUser.getId());

        assertThat(found).isPresent()
                .get()
                .satisfies(u ->
                        assertThat(u.getId()).isEqualTo(testUser.getId()));
    }

    @Test
    void shouldUpdateUser_whenValidDataProvided_updateUserInStorage() {
        testUser.setName("Updated Name");
        testUser.setEmail("updated@example.com");

        User updated = userStorage.update(testUser);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");

        User reloaded = userStorage.findById(testUser.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Updated Name");
        assertThat(reloaded.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void shouldDeleteUser_whenUserExists_removeUserFromStorage() {
        assertThat(userStorage.findById(testUser.getId())).isPresent();

        userStorage.deleteById(testUser.getId());

        assertThat(userStorage.findById(testUser.getId())).isEmpty();
    }
}
