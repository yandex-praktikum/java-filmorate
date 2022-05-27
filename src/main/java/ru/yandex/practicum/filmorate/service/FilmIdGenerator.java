package ru.yandex.practicum.filmorate.service;

public class FilmIdGenerator {
    private static long id = 0;

    public static long generate() {
        return ++id;
    }
}
