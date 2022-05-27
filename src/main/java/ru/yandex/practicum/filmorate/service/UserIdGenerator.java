package ru.yandex.practicum.filmorate.service;

public class UserIdGenerator {
    private static long id = 0;

    public static long generate() {
        return ++id;
    }
}

