package ru.yandex.practicum.filmorate.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@Profile("memFilms")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);

    @Override
    public Film create(Film film) {
        film.setId(nextId.incrementAndGet());
        if (film.getLikes() == null) film.setLikes(new HashSet<>());
        if (film.getGenres() == null) film.setGenres(new LinkedHashSet<>());
        films.put(film.getId(), film);
        return film;
    }


    @Override
    public Film update(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Film " + newFilm.getId() + " not found");
        }
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Optional<Film> findById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public void deleteById(long id) {
        if (films.remove(id) == null) {
            throw new NotFoundException("Film " + id + " not found");
        }
    }

    @Override
    public Film addLike(long filmId, long userId) {
        Film film = getOrThrow(filmId);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        boolean added = film.getLikes().add(userId);
        if (added) {
            films.put(filmId, film);
        }
        return film;
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        Film film = getOrThrow(filmId);
        boolean removed = film.getLikes().remove(userId);
        if (removed) {
            films.put(filmId, film);
        }
        return film;
    }

    @Override
    public Collection<Film> findPopular(int count) {
        return films.values().stream()
                .sorted(
                        Comparator
                                .comparingInt((Film f) -> f.getLikes() == null ? 0 : f.getLikes().size())
                                .reversed()
                                .thenComparingLong(Film::getId)
                )
                .limit(count)
                .collect(Collectors.toList());
    }


    private Film getOrThrow(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Film " + id + " not found");
        }
        return film;
    }
}