package ru.yandex.practicum.filmorate.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.GenreStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreStorage genreStorage;

    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен GET-запрос к эндпоинту /genres");
        return genreStorage.getGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable int genreId) {
         return genreStorage.getGenreById(genreId);
             }
}
