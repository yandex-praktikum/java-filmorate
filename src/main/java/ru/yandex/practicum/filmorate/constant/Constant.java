package ru.yandex.practicum.filmorate.constant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String SIZE_OF_FILMS = "10";
    public static final String REGEX_LOGIN = "^\\S*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final LocalDate REGEX_DATE = LocalDate.of(1895, 12, 28);
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
