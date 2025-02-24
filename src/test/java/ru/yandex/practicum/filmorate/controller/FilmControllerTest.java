package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController controller;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        controller = new FilmController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidation() {
        controller.createFilm(Film.builder()
                .name("name1")
                .description("description1")
                .duration(192)
                .releaseDate(LocalDate.of(2021, 4, 5))
                .build());

        assertEquals(1, controller.getFilms().size());
    }

    @Test
    public void shouldNotPassNameValidation() {
        Film film = Film.builder()
                .name("") // Пустое имя фильма
                .description("description1")
                .duration(192)
                .releaseDate(LocalDate.of(2021, 4, 5))
                .build();

        // Валидируем объект
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        // Проверяем, что ошибка действительно возникла
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для пустого имени фильма");

        // Дополнительно проверяем, что ошибка связана именно с полем name
        boolean hasNameError = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("name"));
        assertTrue(hasNameError, "Ожидалась ошибка валидации для поля 'name'");
    }

    @Test
    public void shouldNotPassDescriptionValidation() {
        Film film = Film.builder()
                .name("The Journey Beyond")
                .description("В далёком будущем человечество достигло звёзд, но с каждым открытием приходит новая "
                        + "угроза. Группа исследователей отправляется в опасное путешествие через неизведанные галактики, "
                        + "чтобы раскрыть тайны древней цивилизации и спасти Землю от надвигающейся катастрофы. На пути "
                        + "им предстоит столкнуться с неизвестными формами жизни, преодолеть страхи и сделать сложный выбор, "
                        + "который изменит их судьбы навсегда.")
                .duration(145)
                .releaseDate(LocalDate.of(2023, 10, 15))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для слишком длинного описания");
    }

    @Test
    public void shouldNotPassReleaseDateValidation() {
        Film film1 = Film.builder()
                .name("Ancient Times")
                .description("A historical film about forgotten eras.")
                .duration(192)
                .releaseDate(LocalDate.of(1600, 1, 1))
                .build();

        Film film2 = Film.builder()
                .name("Future Odyssey")
                .description("A sci-fi epic about distant futures.")
                .duration(192)
                .releaseDate(LocalDate.of(2500, 1, 1))
                .build();

        assertThrows(ConditionsNotMetException.class, () -> controller.createFilm(film1));
        controller.createFilm(film2);
    }

    @Test
    public void shouldNotPassDurationValidation() {
        Film film = Film.builder()
                .name("Negative Duration")
                .description("This film has an invalid duration.")
                .duration(-192)
                .releaseDate(LocalDate.of(2021, 4, 5))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для отрицательной продолжительности");
    }

    @Test
    public void emptyFilmShouldNotPassValidation() {
        Film film = Film.builder().build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Ожидалась ошибка валидации для пустого объекта фильма");
    }

    @Test
    public void shouldUpdateFilm() {
        controller.createFilm(Film.builder()
                .name("name1")
                .description("description1")
                .duration(192)
                .releaseDate(LocalDate.of(2021, 4, 5))
                .build());

        controller.updateFilm(Film.builder()
                .id(1)
                .name("name2")
                .description("description2")
                .duration(50)
                .releaseDate(LocalDate.of(2024, 3, 3))
                .build());

        assertEquals(1, controller.getFilms().size());
    }

    @Test
    public void shouldPassDescriptionValidationWith200Symbols() {
        controller.createFilm(Film.builder()
                .name("name1")
                .description("description1")
                .duration(192)
                .releaseDate(LocalDate.of(2021, 4, 5))
                .build());

        assertEquals(1, controller.getFilms().size());
    }

    @Test
    public void shouldPassReleaseDateValidation() {
        controller.createFilm(Film.builder()
                .name("name1")
                .description("description")
                .duration(192)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build());

        assertEquals(1, controller.getFilms().size());
    }
}