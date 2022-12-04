package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.Exception;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmStorage;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
public class FilmController {

    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    // добавить фильм
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws Exception {
        return inMemoryFilmStorage.addFilm(film);
    }

    // обновить фильм
    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) throws Exception {
        return inMemoryFilmStorage.changeFilm(film);
    }

    // получить все фильмы
    @GetMapping("/films")
    public List<Film> allFilms(){
        return inMemoryFilmStorage.getAllFilms();
    }

    // изменить - удалить фильм
    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable Integer id){
        inMemoryFilmStorage.deleteFilm(id);
    }

}
