package ru.yandex.prakticum.filmorate.controllers.films.users.controller;
//название не может быть пустым;
//        максимальная длина описания — 200 символов;
//        дата релиза — не раньше 28 декабря 1895 года;
//        продолжительность фильма должна быть положительной.


//private final Integer id;
//private String name;
//private String description;
//private Date releaseDate;
//private Time duration;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.prakticum.filmorate.controllers.films.users.controller.exceptions.ValidationException;
import ru.yandex.prakticum.filmorate.controllers.films.users.model.Film;

import java.sql.Time;
import java.time.LocalDate;

@Slf4j
public class FilmCheck {
    public static boolean filmCheck(Film film){

        if (nameCheck(film.getName())&&
            descriptionCheck(film.getDescription())&&
            releaseDateCheck(film.getReleaseDate())&&
            durationCheck(film.getDuration()))
        {
            return true;
        }else {
            return false;
        }
    }

    private static boolean nameCheck(String name){
        if (name.isBlank()){
            log.error("Пустое название фильма");
            throw new ValidationException("Пустое название фильма");
        }
        return true;
    }

    private static boolean descriptionCheck(String description){
        if (description.length() > 200){
            log.error("Слишком длинное описание");
            throw new ValidationException("Слишком длинное описание");
        }
        return true;
    }
    private static boolean releaseDateCheck(LocalDate release){
        if (release.isBefore(LocalDate.of(1895, 12, 28))){
            log.error("Слишком ранняя дата");
            throw new ValidationException("Слишком ранняя дата");
            }
        return true;
    }

    private static boolean durationCheck (Time duration){
        if (duration.getTime() < 0){
            log.error("Продолжительность фильма меньше 0");
            throw new ValidationException("Продолжительность фильма меньше 0");
        }
        return true;
    }

}
