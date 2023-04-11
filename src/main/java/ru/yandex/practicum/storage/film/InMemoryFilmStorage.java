package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.FilmNotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранилище фильмов Filmorate в памяти
 */
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private int id = 0;

    @Autowired
    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    /**
     * Получение всех фильмов
     */
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    /**
     * Добавление фильма
     */
    public Film add(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка валидации даты релиза: {} ", film.getReleaseDate());
            throw new ValidationException("Ошибка валидации даты релиза");
        } else {
            film.setId(++id);
            films.put(film.getId(), film);
        }
        return film;
    }

    /**
     * Обновление фильма
     */
    public Film update(Film film) {
        log.debug("Фильм к обновлению: {}", film);
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза выходит за рамки допустимого");
            throw new ValidationException("Дата релиза выходит за рамки допустимого");
        } else if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            log.warn("Фильма с таким id не найдено");
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    /**
     * Получение фильма по id
     */
    public Film findFilmById(Integer id) {
        Film film = films.get(id);
        if (film != null) {
            log.debug("Фильм по id: {}", film);
            return film;
        } else {
            log.warn("Фильма с таким id не найдено");
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id));
        }
    }
}
