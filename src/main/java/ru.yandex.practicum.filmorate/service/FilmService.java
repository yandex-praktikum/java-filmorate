package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Transactional
    public Film create(Film film) {
        validateMpaAndGenre(film);
        Film created = filmStorage.create(fillGenres(film));
        log.info("Film created: id={}, name='{}'", created.getId(), created.getName());
        return created;
    }

    @Transactional
    public Film update(Film film) {
        ensureFilmExists(film.getId());
        validateMpaAndGenre(film);
        Film updated = filmStorage.update(fillGenres(film));
        log.info("Film updated: id={}", updated.getId());
        return updated;
    }

    @Transactional(readOnly = true)
    public Film findById(long id) {
        Film film = ensureFilmExists(id);
        log.debug("Film fetched: id={}", id);
        return film;
    }

    @Transactional(readOnly = true)
    public Collection<Film> findAll() {
        Collection<Film> all = filmStorage.findAll();
        log.info("Get all films: count={}", all.size());
        return all;
    }

    @Transactional
    public void deleteById(long id) {
        filmStorage.deleteById(id);
        log.info("Film deleted: id={}", id);
    }

    @Transactional
    public Film addLike(long filmId, long userId) {
        ensureFilmExists(filmId);
        ensureUserExists(userId);
        return filmStorage.addLike(filmId, userId);
    }

    @Transactional
    public Film removeLike(long filmId, long userId) {
        ensureFilmExists(filmId);
        ensureUserExists(userId);
        return filmStorage.removeLike(filmId, userId);
    }

    @Transactional(readOnly = true)
    public Collection<Film> getPopular(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        return filmStorage.findPopular(count);
    }

    private Film ensureFilmExists(long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Film not found: id=" + id));
    }

    private void ensureUserExists(long id) {
        userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: id=" + id));
    }

    private void validateMpaAndGenre(Film f) {
        if (f.getMpa() != null && f.getMpa().getId() != null) {
            if (!mpaService.existsById(f.getMpa().getId()))
                throw new NotFoundException("MPA not found: id=" + f.getMpa().getId());
        }
        var genreIds = f.getGenres() == null ? Set.<Integer>of()
                : f.getGenres().stream()
                .filter(Objects::nonNull)
                .map(Genre::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var missing = genreService.findMissingIds(genreIds);
        if (!missing.isEmpty())
            throw new NotFoundException("Genres not found: " + missing);
    }

    private Film fillGenres(Film f) {
        if (f.getGenres() == null || f.getGenres().isEmpty()) return f;
        var filled = f.getGenres().stream()
                .filter(Objects::nonNull)
                .filter(g -> g.getId() != null)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparingInt(Genre::getId))
                ));
        f.setGenres(filled);
        return f;
    }
}
