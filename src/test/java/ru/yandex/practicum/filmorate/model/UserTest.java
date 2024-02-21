package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserTest {
    private Validator validator;
    private UserStorage controller;

    @BeforeEach
    public void startttMethod() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        controller = new InMemoryUserStorage();
    }

    @Test
    public void whenEmailIsInvalid_thenValidationFails() {
        User user = User.builder()
                .email("invalid-email")
                .login("ValidLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "email");
        assertFalse(violations.isEmpty());
        assertEquals("В 'email' использованы запрещённые символы", violations.iterator().next().getMessage());
    }

    @Test
    public void whenLoginIsInvalid_thenValidationFails() {
        User user = User.builder()
                .email("valid.email@example.com")
                .login("")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        controller.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void whenBirthdayIsInTheFuture_thenValidationFails() {
        User user = User.builder()
                .email("valid.email@example.com")
                .login("ValidLogin")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validateProperty(user, "birthday");
        assertFalse(violations.isEmpty());
        assertEquals("День рождения не может быть указан в будущем", violations.iterator().next().getMessage());
    }
}