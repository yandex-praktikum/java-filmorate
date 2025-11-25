package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findById(int id) {
        return genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found"));
    }

    public Set<Integer> findMissingIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Set.of();
        return genreStorage.findMissingIds(ids);
    }
}
