package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    InMemoryFilmStorage inMemoryFilmStorage;
    @Autowired
    InMemoryUserStorage inMemoryUserStorage;
    private final Map<Integer, Set<Integer>> everyFilmWithLikes = new HashMap<>();

    public void addLike(Integer id, Integer userId) {
        Set<Integer> thisFilmLikes = everyFilmWithLikes.get(id);
        if (thisFilmLikes == null) {
            thisFilmLikes = new HashSet<>();
        }
        if (inMemoryUserStorage.getUser(userId) != null) {
            thisFilmLikes.add(userId);
        }
        everyFilmWithLikes.put(id, thisFilmLikes);
    }

    public void deleteLike(Integer id, Integer userId) {
        Set<Integer> thisFilmLikes = everyFilmWithLikes.get(id);
        if (inMemoryUserStorage.getUser(userId) != null) {
            thisFilmLikes.remove(userId);
        }
        if (thisFilmLikes.size() == 0) {
            everyFilmWithLikes.remove(id);
        } else {
            everyFilmWithLikes.put(id, thisFilmLikes);
        }
    }

    public Map<Integer, ArrayList<Film>> sortByLikes() {
        Map<Integer, ArrayList<Film>> likesAndFilms = new HashMap<>();
        for (Integer filmId : everyFilmWithLikes.keySet()) {
            Integer likes = everyFilmWithLikes.get(filmId).size();
            if (!likesAndFilms.containsKey(likes)) {
                ArrayList<Film> newLikeList = new ArrayList<>();
                newLikeList.add(inMemoryFilmStorage.getFilm(filmId));
                likesAndFilms.put(likes, newLikeList);
            } else {
                ArrayList<Film> likeList = likesAndFilms.get(likes);
                likeList.add(inMemoryFilmStorage.getFilm(filmId));
                likesAndFilms.put(likes, likeList);
            }
        }
        return likesAndFilms;
    }

    public List<Film> showTopFilms(int limitOfTop) {
        if (everyFilmWithLikes.isEmpty()) {
            return inMemoryFilmStorage.findAll().stream().limit(limitOfTop).collect(Collectors.toList());
        } else {
            Map<Integer, ArrayList<Film>> sortedByLikes = sortByLikes();
            Map<Integer, ArrayList<Film>> sortedMap = new TreeMap<>(Comparator.reverseOrder());
            sortedMap.putAll(sortedByLikes);
            return sortedMap.values().stream().flatMap(List::stream).limit(limitOfTop).collect(Collectors.toList());
        }
    }
}