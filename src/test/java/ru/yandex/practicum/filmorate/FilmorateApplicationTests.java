package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
//import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class FilmorateApplicationTests {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	public void testValidateEmailUser() throws IOException {
		User user = new User();
		user.setEmail("@exampleexample.com");
		user.setLogin("exampleLogin");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2000, 1, 1));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		Assertions.assertFalse(violations.isEmpty(), "Пользователь с неправильным емейлом не должен создаваться");
	}

	@Test
	public void testValidateLoginUser() throws IOException {
		User user = new User();
		user.setEmail("example@example.com");
		user.setLogin("");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2000, 1, 1));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		Assertions.assertFalse(violations.isEmpty(), "Пользователь с пустым логином не должен создаваться");
	}

	@Test
	public void testValidateDateUser() throws IOException {
		User user = new User();
		user.setEmail("example@example.com");
		user.setLogin("exampleLogin");
		user.setName("Example");
		user.setBirthday(LocalDate.of(2050, 1, 1));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		Assertions.assertFalse(violations.isEmpty(), "Пользователь с датой др в будущем не должен создаваться");
	}

	@Test
	public void testValidateFilmName() throws IOException {
		Film film = new Film();
		film.setName("");
		film.setDescription("exampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		Assertions.assertFalse(violations.isEmpty(), "Фильм с пустым именем не должен создаваться");
	}

	@Test
	public void testValidateFilmDescription() throws IOException {
		Film film = new Film();
		film.setName("Example");
		film.setDescription("exampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilmFilmexampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		Assertions.assertFalse(violations.isEmpty(), "Фильм с описанием более 200 смволов не должен создаваться");
	}

//	@Test(expected = ValidationException.class)
//	public void testValidateFilmRelease() throws IOException {
//		Film film = new Film();
//		film.setName("Example");
//		film.setDescription("exampleFilm");
//		film.setReleaseDate(LocalDate.of(1895, 12, 27));
//		film.setDuration(100);
//		FilmController filmController = new FilmController(new FilmService());
//		filmController.createFilm(film);
//	}

	@Test
	public void testValidateFilmDuration() throws IOException {
		Film film = new Film();
		film.setName("Example");
		film.setDescription("exampleFilm");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(0);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty(), "Фильм с продолжительностью 0 не должен создаваться");
	}

}
