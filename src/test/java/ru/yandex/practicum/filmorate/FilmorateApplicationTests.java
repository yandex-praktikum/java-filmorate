package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void shouldValidateNewFilmFields() {
		FilmController filmController = new FilmController();
		String emptyName = "";
		final ValidationException exception1 = assertThrows(ValidationException.class,
				() -> filmController.createFilm(new Film(
						12, emptyName
						, "desc", LocalDate.of(2012, 12, 12)
						, 12))
		);
		LocalDate older = LocalDate.of(1895, 12, 28).minusDays(1);
		final ValidationException exception2 = assertThrows(ValidationException.class,
				() -> filmController.createFilm(new Film(
						12, "name", "desc", older, 12))
		);
		String longDesc = "w".repeat(201);
		final ValidationException exception3 = assertThrows(ValidationException.class,
				() -> filmController.createFilm(new Film(
						12, "name", longDesc, LocalDate.of(2012, 12, 12)
						, 12))
		);
		int negativeDuration = -3;
		final ValidationException exception4 = assertThrows(ValidationException.class,
				() -> filmController.createFilm(new Film(
						12, "name", "dfr", LocalDate.of(2012, 12, 12)
						, negativeDuration))
		);
		assertAll(
			() -> assertEquals("Наименование фильма не может быть пустым.", exception1.getMessage()),
			() -> assertEquals("Слишком старая дата фильма.", exception2.getMessage()),
			() -> assertEquals("Максимальная длина описания — 200 символов.", exception3.getMessage()),
			() -> assertEquals("Продолжительность фильма должна быть положительной.", exception4.getMessage())
		);
	}

}
