package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collection;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import java.util.Map;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        correctDataOfFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Новый фильм успешно добавлен: ID = {}, Название фильма = {}, Описание фильма = {}, " +
                        "Дата релиза = {}, Длительность = {}",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Не указан Id фильма");
            throw new ValidationException("Id фильма должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.info("Фильм для обновления данных найден: Название фильма = {}, ID = {}", oldFilm.getName(), oldFilm.getId());
            correctDataOfFilm(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Данные фильма успешно обновлены: ID = {}, Название фильма = {}, Описание фильма = {}, " +
                            "Дата релиза = {}, Длительность = {}",
                    oldFilm.getId(), oldFilm.getName(), oldFilm.getDescription(), oldFilm.getReleaseDate(), oldFilm.getDuration());
            return oldFilm;
        }
        log.warn("Ошибка при обновлении фильма: фильм с ID = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @GetMapping
    public Collection<Film> giveAllFilms() {
        return films.values();
    }


    private void correctDataOfFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Ошибка: пустое название фильма");
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка: превышен лимит символов(200)");
            throw new ValidationException("Максимальная длина описания фильма — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка: релиз раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка: продолжительность фильма меньше нуля");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}