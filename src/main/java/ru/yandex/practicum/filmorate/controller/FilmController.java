package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.ErrorMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private final FilmService filmService;


    @GetMapping
    public HashMap<Integer, Film> getAllFIlm() {

        return filmService.getAllFilms();
    }

    @PostMapping
    public ResponseEntity<String> addFilm(@RequestBody Film film) {

        return filmService.addFilms(film);
    }

    @PutMapping
    @ExceptionHandler
    public ResponseEntity<String> updateFilm(@RequestBody Film film) throws NotFoundException {
                 //return filmService.updateFilms(film);
                 return filmService.updateFilms(film);
         }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }


}
