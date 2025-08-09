package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmTests {

    private final Validator validator;

    public FilmTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testCorrectFilm() {

        Film film = new Film();
        film.setName("TestName");
        film.setDescription("TestDescription");
        film.setReleaseDate(new Date(100, 1, 1));
        film.setDuration(Duration.ofMinutes(120));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());

    }

    @Test
    public void testInvalidFilm() {
        Film film = new Film();
        film.setName("");
        film.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        film.setReleaseDate(new Date(2000, 1, 1));
        film.setDuration(Duration.ofMinutes(-10));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }

}
