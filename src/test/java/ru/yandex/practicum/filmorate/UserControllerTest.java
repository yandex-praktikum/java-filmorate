package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RestController
public class UserControllerTest {
    private final LocalDate BIRTHDAY_DATE = LocalDate.now();
    UserController userController;

    @BeforeEach
    void userController(){
        userController = new UserController();
    }

    @Test
    public void correctValidationOfUser() {
        User user = new User(0, "email@e.ru", "login", "name", BIRTHDAY_DATE);
        assertEquals("email@e.ru", user.getEmail());
        assertEquals("login", user.getLogin());
        assertEquals("name", user.getName());
        assertEquals(BIRTHDAY_DATE, user.getBirthday());
    }

    @Test
    public void emailCanNotBeEmptyAndMustContainMailSymbol() {
        User user = new User(0, "", "login", "name", BIRTHDAY_DATE);
        checkException(user, "Почта не может быть пустой и должна содержать символ @");

        User user1 = new User(0, "emaile.ru", "login", "name", BIRTHDAY_DATE);
        checkException(user1, "Почта не может быть пустой и должна содержать символ @");
    }

    @Test
    public void loginCanNotContainSpacesOrNotEmptyOrNotNull() {
        User user = new User(0, "email@e.ru", "     ", "name", BIRTHDAY_DATE);
        checkException(user, "Логин не может быть пустым и содержать пробелы");

        User user1 = new User(0, "email@e.ru", null, "name", BIRTHDAY_DATE);
        checkException(user1, "Логин не может быть пустым и содержать пробелы");

        User user2 = new User(0, "email@e.ru", "", "name", BIRTHDAY_DATE);
        checkException(user2, "Логин не может быть пустым и содержать пробелы");
    }

    @Test
    public void emptyOrNullNameShouldBeReplacedWithLogin() {
        User user = new User(0, "email@e.ru", "login123", "", BIRTHDAY_DATE);
        userController.validate(user);
        assertEquals("login123", user.getName());

        userController();
        User user1 = new User(0, "email@e.ru", "login123", null, BIRTHDAY_DATE);
        userController.validate(user1);
        assertEquals("login123", user1.getName());
    }

    @Test
    public void dateOfBirthdayShouldNotBeInTheFuture() {
        User user = new User(0, "email@e.ru", "login", "name",
                BIRTHDAY_DATE.plusYears(20000));
        checkException(user, "Дата рождения не может быть в будущем");
    }

    @Test
    public void updateUserWithAnUnknownId() {
        User user = new User(9999, "email@e.ru", "login123", "name", BIRTHDAY_DATE);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user));

        assertEquals("Пользователь с id=9999 не найден", exception.getMessage());
    }

    public void checkException(User user, String message){
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.validate(user));

        assertEquals(message, exception.getMessage());
    }

}

