package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private Validator validator;
    private UserController controller;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        controller = new UserController();
    }

    @Test
    void invalidEmailShouldFailValidation() {
        User user = new User("invalid", "login", LocalDate.of(2010, 3, 3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidLoginShouldFailValidation() {
        User user = new User("email@mail.ru", " ", LocalDate.of(2010, 3, 3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldUseLoginWhenEmptyName() {
        User user = new User("email@mail.ru", "login", LocalDate.of(2010, 3, 3));
        controller.create(user);

        assertAll(
                () -> assertEquals(user.getId(), controller.findAll().get(0).getId(),
                        "Пользователи не совпадают"),
                () -> assertEquals("login", controller.findAll().get(0).getName(),
                        "Логин не был использован в качестве имени")
        );
    }

    @Test
    void invalidBirthdayShouldFailValidation() {
        User user = new User("email@mail.ru", "login", LocalDate.of(2222, 3, 3));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        User user = new User("email@mail.ru", "login", LocalDate.of(2010, 3, 3));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.update(user)
        );
        assertEquals("Пользователь не найден", exception.getMessage());
        assertTrue(controller.findAll().isEmpty());
    }
}