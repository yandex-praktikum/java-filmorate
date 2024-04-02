package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static Film getFilmWithSettings(String change, String value) {
        final Film film = Film.builder()
                .id(1)
                .name("The Mask")
                .description("The Mask is a 1994 American superhero comedy film directed by Chuck Russell " +
                        "and produced by Bob Engelman from a screenplay by Mike Werb and a story by Michael Fallon " +
                        "and Mark Verheiden.")
                .releaseDate("1994-07-29")
                .duration(97)
                .build();

        switch (change) {
            case "name":
                film.setName(value);
                break;
            case "description":
                film.setDescription(value);
                break;
            case "releaseDate":
                film.setReleaseDate(value);
                break;
            case "duration":
                film.setDuration(Long.parseLong(value));
                break;
        }
        return film;
    }

    private static Stream<Arguments> provideFilmAndConstraint() {
        return Stream.of(
                Arguments.of(getFilmWithSettings("name", "The Mask"), 0),
                Arguments.of(getFilmWithSettings("name", ""), 1),
                Arguments.of(getFilmWithSettings("name", null), 1),
                Arguments.of(getFilmWithSettings("description", String.valueOf(new char[199])), 0),
                Arguments.of(getFilmWithSettings("description", String.valueOf(new char[200])), 0),
                Arguments.of(getFilmWithSettings("description", String.valueOf(new char[201])), 1),
                Arguments.of(getFilmWithSettings("releaseDate", "1895-12-27"), 1),
                Arguments.of(getFilmWithSettings("releaseDate", "1895-12-28"), 0),
                Arguments.of(getFilmWithSettings("releaseDate", "1895-12-29"), 0),
                Arguments.of(getFilmWithSettings("duration", "-1"), 1),
                Arguments.of(getFilmWithSettings("duration", "0"), 1),
                Arguments.of(getFilmWithSettings("duration", "1"), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilmAndConstraint")
    void filmValidationTest(Film film, int constraint) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(constraint, violations.size(), "Film не прошел валидацию");
    }
}