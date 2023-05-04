package ru.yandex.practicum.filmorate.controller.exceptions;

public class ObjectNotExistException extends RuntimeException {
    private final String key;
    private final int id;

    public ObjectNotExistException(String key, int id) {
        this.key = key;
        this.id = id;
    }

    @Override
    public String getMessage() {
        switch (key) {
            case "User":
                return "Пользователя с идентификатором " + id + " не существует.";
            case "Film":
                return "Фильма с идентификатором " + id + " не существует.";
            default:
                return "Объекта с идентификатором " + id + " не существует.";
        }
    }
}
