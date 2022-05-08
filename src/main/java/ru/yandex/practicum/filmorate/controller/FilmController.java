package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private static final int MAX_DESCRIPTION_SIZE = 200;
    private static final LocalDate FIRST_EVER_FILM = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films;
    private static long idCounter = 0;

    public FilmController() {
        films = new HashMap<>();
    }

    private static long createID() {
        return idCounter++;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (films.values().stream()
                .filter(x -> x.getName().equalsIgnoreCase(film.getName()))
                .anyMatch(x -> x.getReleaseDate().equals(film.getReleaseDate()))) {
            log.error(
                    "Фильм '{}' с датой релиза '{}' уже добавлен.",
                    film.getName(),
                    film.getReleaseDate()
            );
            throw new ValidationException("This film already exists");
        } else if (isValidate(film)) {
            film.setId(createID());
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film.getName());
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        /*if (!films.containsKey(film.getId())) {
            log.error("Фильм '{}' с id '{}' не найден в списке!", film.getName(), film.getId());
            throw new ValidationException("The film not found in the map!");
        } */
        if (isValidate(film)) {
            films.put(film.getId(), film);
            log.info("Отредактирован фильм '{}'", film.getName());
        }
        return film;
    }

    @GetMapping("/films")
    public Map<Long, Film> getAllFilms() {
        return films;
    }

    private boolean isValidate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("invalid film name");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_SIZE || film.getDescription().isBlank()) {
            log.error(String.format("Максимальная длина описания — %s символов", MAX_DESCRIPTION_SIZE));
            throw new ValidationException("invalid description");
        } else if (film.getReleaseDate().isBefore(FIRST_EVER_FILM)) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("invalid release date");
        } else if (film.getDuration().isNegative()) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("invalid duration");
        } else {
            return true;
        }
    }
}