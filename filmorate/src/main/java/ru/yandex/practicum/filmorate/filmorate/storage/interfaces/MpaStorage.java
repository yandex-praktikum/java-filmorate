package ru.yandex.practicum.filmorate.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    public List<Mpa> getMpa();

    public Mpa getMpaById(int id);

}
