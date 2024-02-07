package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmDtoMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmDtoMapper filmDtoTransfer;

    @ExceptionHandler(ValidationException.class)
    @PostMapping
    public FilmDto addFilm(@Valid @RequestBody FilmDto filmDto) {
        Film film;
        if (filmDto != null && !filmDto.getDuration().isNegative()) {
            film = filmService.addFilm(filmDtoTransfer.dtoToFilm(filmDto));
            log.info("Фильм " + film.getName() + "успешно добавлен!");
        } else {
            throw new ValidationException("Продолжительность фильма не должна быть меньше нуля!");
        }
        return filmDtoTransfer.filmToDto(film);
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody FilmDto filmDto) {
        Film film = filmService.updateFilm(filmDtoTransfer.dtoToFilm(filmDto));
        log.info("Фильм " + film.getName() + " успешно обновлён!");
        return filmDtoTransfer.filmToDto(film);
    }

    @GetMapping
    public List<FilmDto> getAllFilms() {
        log.info("Получен список всех фильмов!");
        return filmService.getAllFilms()
                .stream()
                .map(filmDtoTransfer::filmToDto)
                .collect(Collectors.toList()); //
    }
}
