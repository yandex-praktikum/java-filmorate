package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
	static Validator validator;
	UserController userController;
	FilmController filmController;

	@BeforeAll
	static void buildValidator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.usingContext().getValidator();
	}

	@Test
	void user_validation_correct() {
		User user = new User(null, "aaa@mail.com", null, null, LocalDate.of(1990, 10, 03));
		assertFalse(validator.validate(user).size() == 0, "не корректная валидация по пустому логину");

		User user1 = new User(null, "aaa@mail.com", "login login", null, LocalDate.of(1990, 10, 03));
		assertFalse(validator.validate(user1).size() == 0, "не корректная валидация по логину с пробелами");

		User user2 = new User(null, null, "login", null, LocalDate.of(1990, 10, 03));
		assertFalse(validator.validate(user2).size() == 0, "не корректная валидация по пустой почте");

		User user3 = new User(null, "это-неправильный?эмейл@", "login", null, LocalDate.of(1990, 10, 03));
		assertFalse(validator.validate(user3).size() == 0, "не корректная валидация по формату почты");

		User user4 = new User(null, "aaa@mail.com", "login", "Mortie", LocalDate.of(2222, 10, 03));
		assertFalse(validator.validate(user4).size() == 0, "не корректная валидация по неверной дате");

		User user5 = new User(null, "aaa@mail.com", "login", "Mortie", LocalDate.of(1990, 10, 03));
		assertTrue(validator.validate(user5).size() == 0, "отсеивается корректный пользователь");
	}

	@Test
	void film_validation_correct() {
		Film film = new Film(null, "AAA BBB CCC", "description", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		assertTrue(validator.validate(film).size() == 0, "отсеивается корректный фильм");

		Film film1 = new Film(null, null, "description", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		assertFalse(validator.validate(film1).size() == 0, "не корректная валидация по пустому имени");

		Film film2 = new Film(null, "AAA BBB CCC",
				"KgIGmWllKP2ysubdsPcJelTnLe08qxRZ7fYQ6B5ISLOgJxnxw9qA4B7FMexiTDoqGGJenXN9D8KaGwgGg0onl" +
						"VrADNnHi9PUAV4XPJsafP09pTYy4HUTYoe3Ju2SDIYvZfGemqskAWuASKlNoKUTYva31VzYp7ukuvSJf8x7PsQ" +
						"ddfh7mzxcUmBPY7tZtrcD4IGh6Qe4GqyT0qBMAxPJf6voqGOweOkOMCSE406JsZ3FIRMsPa87Uhp",
				LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		assertFalse(validator.validate(film2).size() == 0, "не корректная валидация при количестве символов более 200 в описании");

		Film film3 = new Film(null, "AAA BBB CCC", "description", LocalDate.of(1990, 10, 03), Duration.ZERO);
		assertFalse(validator.validate(film3).size() == 0, "Некорректная валидация при длительности ноль");

		Film film4 = new Film(null, "AAA BBB CCC", "description", LocalDate.of(1000, 10, 03), Duration.ofMinutes(120));
		assertFalse(validator.validate(film4).size() == 0, "Некорректная валидация неверной даты релиза");
	}

	@Test
	void user_creation_correct() {
		userController = new UserController();

		User user = new User(null, "aaa@mail.com", "login", "Mortie", LocalDate.of(1990, 10, 03));
		User user1 = new User(null, "bbb@mail.com", "login", null, LocalDate.of(1990, 10, 03));
		userController.createUser(user);
		userController.createUser(user1);
		user.setId(1);
		user1.setId(2);
		user1.setName(user1.getLogin());

		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(user);
		expectedUsers.add(user1);

		assertEquals(expectedUsers, userController.getUsers(), "не корректно создается пользователь");
		assertEquals(expectedUsers.get(1), userController.getUsers().get(1), "пустое имя пользователя не меняется на логин");
		assertThrows(ValidationException.class, () -> userController.createUser(user1), "имеющиеся пользователи дублируются");
	}

	@Test
	void user_update_correct() {
		userController = new UserController();

		User user = new User(null, "aaa@mail.com", "login", "Mortie", LocalDate.of(1990, 10, 03));
		userController.createUser(user);

		User user1 = new User(1, "aaa@mail.com", "login1", "Mortie", LocalDate.of(1990, 10, 03));
		userController.updateUser(user1);

		assertEquals("login1", userController.getUsers().get(0).getLogin(), "обновление проходит не корректно");

		User user2 = new User(1000, "aaa@mail.com", "login", "Mortie", LocalDate.of(1990, 10, 03));
		assertThrows(ValidationException.class, () -> userController.updateUser(user2), "обновляется несуществующий юзер");
	}

	@Test
	void film_creation_correct() {
		filmController = new FilmController();

		Film film = new Film(null, "AAA", "description", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		Film film1 = new Film(null, "BBB", "description", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		filmController.createFilm(film);
		filmController.createFilm(film1);
		film.setId(1);

		List<Film> expectedFilms = new ArrayList<>();
		expectedFilms.add(film);
		expectedFilms.add(film1);

		assertEquals(expectedFilms, filmController.getFilms(), "не корректно создается фильм");

		Film film2 = new Film(null, "CCC", "description", LocalDate.of(1000, 10, 03), Duration.ofMinutes(120));
		assertEquals(expectedFilms, filmController.getFilms(), "не отсеивается по дате релиза");
	}

	@Test
	void film_update_correct() {
		filmController = new FilmController();

		Film film = new Film(null, "AAA", "description", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		filmController.createFilm(film);

		Film film1 = new Film(1, "AAA", "description1", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		filmController.updateFilm(film1);

		assertEquals("description1", filmController.getFilms().get(0).getDescription(), "обновление проходит не корректно");

		Film film2 = new Film(1000, "AAA", "description1", LocalDate.of(1990, 10, 03), Duration.ofMinutes(120));
		assertThrows(ValidationException.class, ()-> filmController.updateFilm(film2), "обновляется несуществующий фильм");
	}
}
