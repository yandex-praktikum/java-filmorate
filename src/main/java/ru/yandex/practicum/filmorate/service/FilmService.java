package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    /**
     * Method returns all films.
     *
     * @return List of all films.
     */
    List<Film> findAll();

    /**
     * Method returns film from repository by id.
     *
     * @param id Id of film to be found.
     *
     * @return film with requested id.
     */
    Film findById(Integer id);

    /**
     * Method adds film to repository.
     *
     * @param film Film to be added.
     *
     * @return Added film with assigned ID.
     */
    Film create(Film film);

    /**
     * Method updates existing film in the repository.
     *
     * @param film Film to be updated.
     *
     * @return Updated film.
     */
    Film update(Film film);
}
