package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.model.User;


@SpringBootTest
class FilmorateApplicationTests {

	FilmController filmController;
	UserController userController;

	@Test
	void contextLoads() {
	}

	@Test
	void addFilm () throws ValidationException {
		Film film = Film.builder()
				.name("Name")
				.description("description")
				.releaseDate(LocalDate.of(2000,10,10))
				.duration(100)
				.build();
		filmController = new FilmController();
		filmController.addFilm(film);
		Assertions.assertEquals(film, filmController.getFilms().get(0));
	}

	@Test
	void addUser() throws ValidationException {
		User user = User.builder()
				.email("email@email.ru")
				.login("Login")
				.name("name")
				.birthday(LocalDate.of(2000,6,10))
				.build();
		userController = new UserController();
		userController.addUser(user);
		Assertions.assertEquals(user, userController.getUsers().get(0));
	}
}
