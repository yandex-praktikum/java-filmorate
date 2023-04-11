package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервисный класс фильмов Filmorate
 */
@Slf4j
@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    /**
     * Установка лайка фильму
     */
    public Film putLike(Film film, Integer id) {
        log.debug("Добавление лайка фильму {} ", film);
        film.getLikes().add(id);
        return film;
    }

    /**
     * Удаление лайка фильма
     */
    public Film deleteLike(Integer filmId, Integer id) {
        Film film = storage.findFilmById(filmId);
        log.debug("Удаление лайка у фильма {}", film);
        Set<Integer> likes = film.getLikes();
        if (film != null && likes.contains(id)) {
            film.getLikes().remove(id);
        } else {
            log.warn("Лайк с таким id не найден");
            throw new RuntimeException("Лайк с таким id не найден");
        }
        return film;
    }

    /**
     * Получение популярных фильмов
     */
    public List<Film> findBest(Integer count) {
        log.debug("Получение {} популярных фильмов", count);
        return storage.findAll()
                .stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
