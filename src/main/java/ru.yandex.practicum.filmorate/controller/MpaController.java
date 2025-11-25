package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public ResponseEntity<Collection<MpaRating>> getAll() {
        return ResponseEntity.ok(mpaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaRating> getById(@PathVariable int id) {
        return ResponseEntity.ok(mpaService.findById(id));
    }
}
