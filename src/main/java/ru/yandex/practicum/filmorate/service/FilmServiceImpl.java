package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService{

    private final FilmStorage filmStorage;

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        checkFilmExist(film.getId());

        return filmStorage.update(film);
    }

    private void checkFilmExist(Integer id) {
        if (filmStorage.findById(id) == null) {
            throw new NotFoundException(String.format("Film with id = %d is not found.", id));
        }
    }
}
