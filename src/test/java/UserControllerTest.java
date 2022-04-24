import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controllers.UserController;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private User user = new User();

    @BeforeEach
    void createUser() {

        user.setName("Name");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(2020, 01, 01));
        user.setEmail("test@test");
    }

    @Test
    void shouldNotCreateUserWhileEmailIsBlank() {
        user.setEmail("");

        UserController userController = new UserController();
        String message = null;

        try {
            userController.create(user);
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        assertEquals(message, "Не валидный адрес электронной почты.");
    }

    @Test
    void shouldNotCreateUserWhileEmailNotContainsSymbol() {
        user.setEmail("test");

        UserController userController = new UserController();
        String message = null;

        try {
            userController.create(user);
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        assertEquals(message, "Не валидный адрес электронной почты.");
    }

    @Test
    void shouldNotCreateUserWithSpacesInLogin() {
        user.setLogin("Na me");

        UserController userController = new UserController();
        String message = null;

        try {
            userController.create(user);
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        assertEquals(message, "Логин не может быть пустым и содержать пробелы.");
    }

    @Test
    void shouldNotCreateUserWithEmptyLogin() {
        user.setLogin("");

        UserController userController = new UserController();
        String message = null;

        try {
            userController.create(user);
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        assertEquals(message, "Логин не может быть пустым и содержать пробелы.");
    }

    @Test
    void nameShouldEqualsLoginIfNameIsEmpty() {
        user.setName("");

        UserController userController = new UserController();

        userController.create(user);

        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void nameShouldCreateUserIfBirthdayAfterToday() {
        // дата рождения больше сегодняшней на 1 день
        user.setBirthday(LocalDate.now().plus(Period.ofDays(1)));

        UserController userController = new UserController();
        String message = null;

        try {
            userController.create(user);
        } catch (ValidationException e) {
            message = e.getMessage();
        }

        assertEquals(message, "Дата рождения не может быть в будущем.");
    }

    @Test
    void shouldCreateUserIfBirthdayIsTodayOrLess() {
        user.setBirthday(LocalDate.now());

        UserController userController = new UserController();

        userController.create(user);

        assertEquals(userController.getUsers().size(), 1);
    }
}

