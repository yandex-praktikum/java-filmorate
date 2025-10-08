package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

public interface RatingStorage {

    Rating createRating(Rating rating);

    Rating getRatingById(Long id);

    Collection<Rating> getAllRatings();
}
