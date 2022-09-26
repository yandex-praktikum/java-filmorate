package ru.yandex.practicum.filmorate.exception;

public class InvalidDescriptionException extends Throwable {
    public InvalidDescriptionException() {
        System.out.println("Ошибка InvalidDescriptionException: описание фильма не может быть длиннее 200 символов");
    }

    public String getMessage() {
        return "Ошибка InvalidDescriptionException: описание фильма не может быть длиннее 200 символов";
    }
}
