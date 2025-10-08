package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Rating createRating(Rating rating) {
        return ratingStorage.createRating(rating);
    }

    public Rating getRatingById(Long id) {
        return ratingStorage.getRatingById(id);
    }

    public Collection<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }
}
