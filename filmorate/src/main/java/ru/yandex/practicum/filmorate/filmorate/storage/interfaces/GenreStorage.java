package ru.yandex.practicum.filmorate.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    public List<Genre> getGenres();

    public Genre getGenreById(int id);
}
