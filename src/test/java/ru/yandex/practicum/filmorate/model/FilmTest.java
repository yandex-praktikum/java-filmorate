package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {

    private Validator validator;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validFilm = new Film();
        validFilm.setName("Валидный фильм");
        validFilm.setDescription("Допустимое описание длиной менее 200 символов");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        validFilm.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        validFilm.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenDescriptionIsTooLong() {
        String longDescription = "A".repeat(201);
        validFilm.setDescription(longDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldPassWhenDescriptionIsExactly200Characters() {
        String exactLengthDescription = "A".repeat(200);
        validFilm.setDescription(exactLengthDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(0, violations.size(), "Описание длиной 200 символов должно быть валидным");
    }

    @Test
    void shouldPassWhenReleaseDateIsExactlyMinDate() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(0, violations.size(), "Дата релиза 28.12.1895 должна быть валидной");
    }

    @Test
    void shouldFailWhenDurationIsNegative() {
        validFilm.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenDurationIsZero() {
        validFilm.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldPassWhenDurationIsPositive() {
        validFilm.setDuration(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(0, violations.size(), "Продолжительность 1 должна быть валидной");
    }

    @Test
    void shouldFailWhenReleaseDateIsInFuture() {
        validFilm.setReleaseDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(1, violations.size());
        assertEquals("Дата релиза не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    void shouldPassWhenReleaseDateIsToday() {
        validFilm.setReleaseDate(LocalDate.now());
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertEquals(0, violations.size(), "Дата релиза сегодня должна быть валидной");
    }
}