package ru.yandex.practicum.filmorate.controller.exceptions;

public class ObjectAlreadyExistException extends RuntimeException {
    private final String key;
    private final int id;

    public ObjectAlreadyExistException(String key, int id) {
        this.key = key;
        this.id = id;
    }

    @Override
    public String getMessage() {
        switch (key) {
            case "User":
                return "Пользователь с идентификатором " + id + " уже существует.";
            case "Film":
                return "Фильм с идентификатором " + id + " уже существует.";
            default:
                return "Объект с идентификатором " + id + " уже существует.";
        }
    }
}
