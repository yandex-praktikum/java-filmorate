package ru.yandex.practicum.filmorate.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(Film.class)
public class FilmTests {

    private Validator validator;

    @BeforeEach
    public void setup() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    public void testFilmValidations() {
        Film film = new Film();
        film.setName("Some Film");
        film.setDescription("This is a description of the film that is within the limit of 200 characters.");
        film.setReleaseDate(LocalDate.of(1995, 12, 15));
        film.setDuration(120);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testFilmWithInvalidData() {
        Film film = new Film();
        film.setName(""); // Invalid name
        film.setDescription(""); // Invalid description
        film.setReleaseDate(null); // Invalid release date
        film.setDuration(-10); // Invalid duration

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertTrue(errors.hasErrors());
        assertEquals(4, errors.getErrorCount());
        assertNotNull(errors.getFieldError("name"));
        assertNotNull(errors.getFieldError("description"));
        assertNotNull(errors.getFieldError("releaseDate"));
        assertNotNull(errors.getFieldError("duration"));
    }

    @Test
    public void whenDurationIsNegative_thenViolationOccurs() {
        Film film = new Film();
        film.setName("Some Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-1);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertTrue(errors.hasFieldErrors("duration"));
        assertEquals("Duration must be greater than zero",
                Objects.requireNonNull(errors.getFieldError("duration")).getDefaultMessage());
    }

    @Test
    public void whenReleaseDateIsNull_thenViolationOccurs() {
        Film film = new Film();
        film.setName("Some Film");
        film.setDescription("Valid description");
        film.setReleaseDate(null);
        film.setDuration(120);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertTrue(errors.hasFieldErrors("releaseDate"));
        assertEquals("Release date cannot be null",
                Objects.requireNonNull(errors.getFieldError("releaseDate")).getDefaultMessage());
    }

    @Test
    public void whenDescriptionTooLong_thenViolationOccurs() {
        Film film = new Film();
        film.setName("Some Film");
        film.setDescription("This description is way too long. ".repeat(10));  // Exceeds 200 characters
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertTrue(errors.hasFieldErrors("description"));
        assertEquals("Максимальная длина описания — 200 символов.",
                Objects.requireNonNull(errors.getFieldError("description")).getDefaultMessage());
    }

    @Test
    public void whenNameBlank_thenViolationOccurs() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("Name cannot be blank",
                Objects.requireNonNull(errors.getFieldError("name")).getDefaultMessage());
    }

    @Test
    public void whenFilmValid_thenNoViolations() {
        Film film = new Film();
        film.setName("Valid Film Name");
        film.setDescription("Valid description under 200 characters.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Errors errors = new BeanPropertyBindingResult(film, "film");
        validator.validate(film, errors);

        assertFalse(errors.hasErrors());
    }
}
