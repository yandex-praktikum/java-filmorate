package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final Instant CINEMA_BIRTH_DATE = Instant.parse("1895-12-28T00:00:00Z");

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("GET /films - запрос всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("POST /films, создание нового фильма: {}", film.getName());
        if ((film.getName() == null) || (film.getName().isBlank())) {
            log.warn("Ошибка валидации имени фильма, получен: {}", film.getName());
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка валидации описании фильма, получен: {}", film.getDescription());
            throw new ConditionsNotMetException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH_DATE)) {
            log.warn("Ошибка валидации даты релиза фильма, получен: {}", film.getReleaseDate());
            throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if ((film.getDuration().isNegative()) || (film.getDuration().isZero())) {
            log.warn("Ошибка валидации продолжительности фильма, получен: {}", film.getDuration());
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }

        film.setId(getNextId());
        log.debug("Фильму сгенерирован id: {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм с id {} добавлен во внутреннее хранилеще", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        log.info("PUT /films - обновление пользователя с id: {}", newFilm.getId());
        Long newFilmId = newFilm.getId();
        if (newFilmId == null) {
            log.warn("При изменении фильма не передали id");
            throw new ConditionsNotMetException("id должен быть указан");
        }

        if (films.containsKey(newFilmId)) {
            Film oldFilm = films.get(newFilmId);
            if ((newFilm.getName() == null) || (newFilm.getDescription() == null) || (newFilm.getDuration() == null) ||
                    (newFilm.getReleaseDate() == null)) {
                log.info("Не все данные для изменения пользователя были переданы");
                return oldFilm;
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм с id: {} успешно обновлён", newFilmId);
            return oldFilm;
        }
        log.warn("Фильм с id: {} не был найден", newFilmId);
        throw new NotFoundException("Фильм с данным id не найден");
    }

    private long getNextId() {
        long maxFilmId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++maxFilmId;
    }
}
