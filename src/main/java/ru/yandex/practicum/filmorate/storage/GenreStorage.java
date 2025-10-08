package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    Genre createGenre(Genre genre);

    Genre getGenreById(Long id);

    Collection<Genre> getAllGenres();
}
