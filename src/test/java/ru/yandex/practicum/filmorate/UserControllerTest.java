package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    private final UserController controller = new UserController();
    private final User user = User.builder()
            .id(1)
            .email("yandex@yandex.ru")
            .login("user")
            .name("User")
            .birthday(LocalDate.of(1990, 1, 1))
            .build();

    @Test
    void create_shouldCreateAUser() {
        User thisUser = new User(1, "yandex@yandex.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        controller.create(thisUser);

        Assertions.assertEquals(user, thisUser);
        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void update_shouldUpdateUserData() {
        User thisUser = new User(1, "mail@yandex.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        controller.create(user);
        controller.update(thisUser);

        Assertions.assertEquals("mail@yandex.ru", thisUser.getEmail());
        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void create_shouldCreateAUserIfIdAndNameAreEmpty() {
        User thisUser = new User(0, "mail@yandex.ru", "user", null,
                LocalDate.of(1990, 1, 1));
        controller.create(thisUser);

        Assertions.assertEquals(1, thisUser.getId());
        Assertions.assertEquals("user", thisUser.getName());
    }

    @Test
    void create_shouldThrowExceptionIfEmailIncorrect() {
        user.setEmail("yandex.mail.ru");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldThrowExceptionIfEmailIsEmpty() {
        user.setEmail("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldNotAddUserIfLoginIsEmpty() {
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void create_shouldNotAddUserIfBirthdayIsInTheFuture() {
        user.setBirthday(LocalDate.of(2024, 6, 28));

        Assertions.assertThrows(ValidationException.class, () -> controller.create(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }
}
