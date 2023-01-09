package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmDBService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmDBService(@Qualifier("dbFilmStorage") FilmStorage filmStorage
            , @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    @Override
    public void deleteAll() {
        filmStorage.deleteAll();
    }

    @Override
    public Optional<Film> getById(Long id) {
        return filmStorage.getById(id);
    }

    @Override
    public void addLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null
                && idFilm > 0 && idUser > 0) {
            filmStorage.addLike(idUser, idFilm);
        } else {
            throw new NotFoundException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    @Override
    public void removeLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.removeLike(idUser, idFilm);
        } else {
            throw new ValidateException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    @Override
    public List<Optional<Film>> getMaxRating(Integer countRate) {
        Integer count = countRate;
        if (count == 0 || count == null) {
            count = 10;
        }
        List<Optional<Film>> listOrder = filmStorage.getOrderRate(count);
        return listOrder;
    }

    @Override
    public Mpa getMpa(Long idMpa) {
        return filmStorage.getMpa(idMpa);
    }

    @Override
    public TreeSet<Genre> getGenres(Film film) {
        return filmStorage.getGenres(film);
    }
}
