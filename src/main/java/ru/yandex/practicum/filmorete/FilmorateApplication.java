package ru.yandex.practicum.filmorete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorete.model.Film;
import ru.yandex.practicum.filmorete.model.User;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {

		User user = User.builder()
				.name("Bogdan")
				.birthday(LocalDate.of(1997, 4, 11))
				.email("sinitsa.bogdan.97@yandex.ru")
				.login("SinitsaBogdan")
				.build();

		System.out.println(user);

		Film film = Film.builder()
				.name("Один дома")
				.description("Комедия")
				.releaseDate(LocalDate.of(2002, 6, 24))
				.duration(75)
				.build();

		System.out.println(film);

		SpringApplication.run(FilmorateApplication.class, args);
	}
}
