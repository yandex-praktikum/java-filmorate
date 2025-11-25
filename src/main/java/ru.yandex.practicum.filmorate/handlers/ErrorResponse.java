package ru.yandex.practicum.filmorate.handlers;

public record ErrorResponse(
        String title,
        int status,
        String detail,
        String path
) {
}
