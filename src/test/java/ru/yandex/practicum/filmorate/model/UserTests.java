package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.Errors;
import org.springframework.validation.BeanPropertyBindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    public void whenEmailIsEmpty_thenViolationOccurs() {
        User user = new User();
        user.setEmail("");  // Invalid as it's empty
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("email"));
        assertEquals("Email не может быть пустым", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void whenEmailInvalid_thenViolationOccurs() {
        User user = new User();
        user.setEmail("invalid-email");  // Missing '@'
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("email"));
        assertEquals("Email should be valid", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void whenLoginIsEmptyOrContainsSpaces_thenViolationOccurs() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("invalid login");  // Contains spaces
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("login"));
        assertEquals("Login не должен содержать пробелы", errors.getFieldError("login").getDefaultMessage());
    }



    @Test
    public void whenUserIsValid_thenNoViolations() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }
}