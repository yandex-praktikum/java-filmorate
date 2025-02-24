package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserControllerTest {
    private UserController controller;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        controller = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidation() {
        User user = User.builder()
                .login("login1")
                .name("name1")
                .email("mail1@mail.ru")
                .birthday(LocalDate.of(1979, 3, 25))
                .build();
        controller.create(user);
        System.out.println(user);

        assertEquals(1, controller.getUsers().size());
    }

    @Test
    public void shouldNotPassEmailValidation() {

        User user1 = User.builder()
                .login("login1")
                .name("name1")
                .email("mail1.ru") // Неверный формат email
                .birthday(LocalDate.of(1979, 3, 25))
                .build();

        User user2 = User.builder()
                .login("login2")
                .name("name2")
                .email("") // Пустая строка
                .birthday(LocalDate.of(2003, 6, 8))
                .build();

        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertFalse(violations1.isEmpty(), "Ожидалась ошибка валидации для неверного email");

        // Проверяем валидацию для пустого email
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        assertFalse(violations2.isEmpty(), "Ожидалась ошибка валидации для пустого email");
    }

    @Test
    public void shouldNotPassBirthdayValidation() {
        // Дата рождения в будущем
        User user = User.builder()
                .login("login1")
                .name("name1")
                .email("mail1@mail.ru")
                .birthday(LocalDate.of(2056, 1, 1))
                .build();

        // Проверяем ошибки валидации для будущей даты рождения
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для будущей даты рождения");
    }

    @Test
    public void shouldNotPassLoginValidation() {
        // Логин с пробелами
        User user = User.builder()
                .login("invalid login")
                .name("name1")
                .email("mail1@mail.ru")
                .birthday(LocalDate.of(1990, 5, 15))
                .build();

        // Проверяем ошибки валидации для логина с пробелами
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для логина с пробелами");
    }


@Test
public void shouldUpdateUser() {
    controller.create(User.builder()
            .login("login1")
            .name("name1")
            .email("mail1@mail.ru")
            .birthday(LocalDate.of(2003, 6, 8))
            .build());

    controller.update(User.builder()
            .id(1)
            .login("login2")
            .name("name2")
            .email("mail2@mail.ru")
            .birthday(LocalDate.of(2003, 6, 8))
            .build());

    assertEquals(1, controller.getUsers().size());
}

@Test
public void shouldCreateWithEmptyName() {
    User user = User.builder()
            .login("login1")
            .email("mail1@mail.ru")
            .name("")
            .birthday(LocalDate.of(2003, 6, 8))
            .build();

    controller.create(user);

    assertEquals("login1", user.getName());
}

}