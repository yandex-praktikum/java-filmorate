package ru.yandex.practicum.filmorate.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaStorage mpaStorage;

    public MpaController(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }


    @GetMapping
    public List<Mpa> getMpa() {
        log.info("Получен GET-запрос к эндпоинту /mpa");
        return mpaStorage.getMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpaById(@PathVariable int mpaId) {
        return mpaStorage.getMpaById(mpaId);
    }
}
