package ru.yandex.practicum.filmorate.validator.exception;

public class FilmValidatorException extends ValidatorException {
    public static final String INCORRECT_NAME;
    public static final String INCORRECT_DESCRIPTION;
    public static final String INCORRECT_RELEASE_DATE;
    public static final String INCORRECT_DURATION;

    static {
        INCORRECT_NAME = "Некорректное имя >%s\n"
                + "Имя не может быть пустым.";
        INCORRECT_DESCRIPTION = "Некорректное описание >%s\n"
                + "Максимальная длина описания — 200 символов.";
        INCORRECT_RELEASE_DATE = "Некорректная дата релиза >%s\n"
                + "Дата релиза — не раньше 28 декабря 1895 года;";
        INCORRECT_DURATION = "Некорректная продолжительность >%s\n"
                + "Продолжительность фильма должна быть положительной.";
    }

    public FilmValidatorException() {
    }

    public FilmValidatorException(String message) {
        super(message);
    }
}