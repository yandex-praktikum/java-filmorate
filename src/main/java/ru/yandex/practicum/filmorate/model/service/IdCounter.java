package ru.yandex.practicum.filmorate.model.service;

public class IdCounter {
    //    private static AtomicLong idCounter = new AtomicLong();
    private static int idUserCounter;
    private static int idFilmCounter;

    public static int createUserId() {
        return ++idUserCounter;
    }

    public static int createFilmId() {
        return ++idFilmCounter;
    }

}
