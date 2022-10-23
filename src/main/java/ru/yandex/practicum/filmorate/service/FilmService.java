package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public String putLike(Integer id, Integer userId) {
        if (filmStorage.findFilm(id) != null && userStorage.findUser(userId) != null){
            filmStorage.findFilm(id).getLikes().add(userId);
            return String.format("Фильму с id=%s поставлен лайк пользователем с userId=%s.", id, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    public String deleteLike(Integer id, Integer userId) {
        if (filmStorage.findFilm(id) != null && userStorage.findUser(userId) != null){
            filmStorage.findFilm(id).getLikes().remove(userId);
            return String.format("У фильма с id=%s удалён лайк пользователем с userId=%s.", id, userId);
        }
        throw new IdNotFoundException("Указанные id неверны.");
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> filmList = filmStorage.getFilms();
        return filmList.stream().sorted((po, p1) -> {
            if (po.getLikes().size() > p1.getLikes().size()){
                return -1;
            } else if (po.getLikes().size() < p1.getLikes().size()){
                return 1;
            } return 0;
        }).limit(count).collect(Collectors.toList());
    }

    public List<Film> getFilms(){
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film){
        return filmStorage.createFilm(film);
    }

    public Film findFilm(int id){
        return filmStorage.findFilm(id);
    }

    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    public String deleteFilm(int id) {
        return filmStorage.deleteFilm(id);
    }

}
