package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;

import java.time.LocalDate;

@Component
@Slf4j
public class FilmValidator {

    private static final LocalDate EARLIEST_FILM = LocalDate.of(1895, 12, 28);

    public void validateFilmReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(EARLIEST_FILM)) {
            final String message = "Film release date should not be earlier that 28.12.1985.";
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}
