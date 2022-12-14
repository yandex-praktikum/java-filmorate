package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private static int idFilm = 0;
    private List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        Film filmForList = null;
        System.out.println(film);
        try {
            boolean isCorrect = validateFilm(film);
            if (isCorrect) {
                if (film.getId() == 0) {
                    setIdFilm(getIdFilm() + 1);
                    film.setId(getIdFilm());
                }
                filmForList = film;
                films.add(filmForList);
            }
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getDetailMessage());
        }
        return filmForList;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        try {
            boolean isCorrect = validateFilm(film);
            if (isCorrect) {
                boolean isContain = false;
                if (!films.isEmpty()) {
                    for (Film filmFromList : films) {
                        if (filmFromList.getId() == film.getId()) {
                            films.remove(filmFromList);
                            films.add(film);
                            isContain = true;
                            break;
                        }
                    }
                    if (!isContain) {
                        log.info("Некорректный запрос. Фильм c таким id не найден");
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный запрос. Фильм c таким id не найден");
                    }
                } else {
                    log.info("Фильмы отсутствуют в базе");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильмы отсутствуют в базе");
                }
            }
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getDetailMessage());
        }
        return film;
    }

    private boolean validateFilm(Film film) {
        boolean isCorrect = true;
        if (film.getName().isBlank()) {
            isCorrect = false;
            log.info("Ошибка валидации фильма. Название не может быть пустым");
            throw new ValidationException("Ошибка валидации фильма. Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            isCorrect = false;
            log.info("Ошибка валидации фильма. Максимальная длина описания - 200 символов");
            throw new ValidationException("Ошибка валидации фильма. Максимальная длина описания - 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            isCorrect = false;
            log.info("Ошибка валидации фильма. Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка валидации фильма. Дата релиза не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            isCorrect = false;
            log.info("Ошибка валидации фильма. Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Ошибка валидации фильма. Продолжительность фильма должна быть больше 0");
        }
        return isCorrect;
    }

    public static int getIdFilm() {
        return idFilm;
    }

    public static void setIdFilm(int idFilm) {
        FilmController.idFilm = idFilm;
    }

}