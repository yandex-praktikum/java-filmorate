package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<MpaRating> findAll() {
        return mpaStorage.findAll();
    }

    public MpaRating findById(int id) {
        return mpaStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA with id " + id + " not found"));
    }

    public boolean existsById(int id) {
        return mpaStorage.existsById(id);
    }
}