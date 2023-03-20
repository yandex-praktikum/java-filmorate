package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;

@Service
public interface FilmService {
    Film updateFilm(@Valid Film film);

    Film addFilms(@Valid Film film);

    HashMap<Integer, Film> getAllFilms();
}
