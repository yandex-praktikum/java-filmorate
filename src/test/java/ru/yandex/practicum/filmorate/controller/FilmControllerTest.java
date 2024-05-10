package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    public void whenBirthdayIsInTheFuture_thenViolationOccurs() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));  // Birthday in the future

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("birthday"));
    }


    @Test
    public void whenNameIsEmpty_useLoginAsDisplayName() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("");  // Empty name
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertFalse(errors.hasFieldErrors("name"));
        assertEquals("validLogin", user.getName());  // Expected to use login as display name
    }
}
