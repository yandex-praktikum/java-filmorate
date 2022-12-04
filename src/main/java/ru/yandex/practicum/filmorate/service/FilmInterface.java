package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmInterface {

    Film addFilm(Film film);

    void deleteFilm(Integer idFilm);

    Film changeFilm(Film film);

    List<Film> getAllFilms();

}