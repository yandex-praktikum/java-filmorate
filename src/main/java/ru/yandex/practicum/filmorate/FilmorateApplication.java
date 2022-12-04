package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class FilmorateApplication {


    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);

        Film film1 = new Film(1, "Страна радости", "Вдохновленные фантастикой", LocalDate.of(2022, 9,1), 90);
        FilmStorage storageFilms = new FilmStorage();
        storageFilms.addFilm(film1);
    }

}
