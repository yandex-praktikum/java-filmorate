package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
//название не может быть пустым;
//максимальная длина описания — 200 символов;
//дата релиза — не раньше 28 декабря 1895 года;
//продолжительность фильма должна быть положительной.

@Slf4j
public class FilmDataValidate {
    private Film film;
    private static final int maxLengthDescription = 200;//максимальная длина описания — 200 символов;
    private static final LocalDate dateFirstFilm = LocalDate.of(1895, 12, 28);//дата релиза — не раньше 28 декабря 1895 года;


    public FilmDataValidate (Film film) {
        this.film = film;
    }

    public boolean checkAllData() {
        if(isCorrectName() && isCorrectLengthDescription() && isCorrectReleaseDate() && isPositiveDuration()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCorrectName() {//название не может быть пустым;
        if(!film.getName().isBlank()) {
            return true;
        } else {
            log.warn("Ошибка во входных данных фильма. Пустое название фильма");
            return false;
        }
    }

    private boolean isCorrectLengthDescription() {//максимальная длина описания — 200 символов;
        if(film.getDescription().length() <= maxLengthDescription) {
            return true;
        } else {
            log.warn("Ошибка во входных данных фильма. Превышено максимально допустимое описание фильма в "
                    + maxLengthDescription + " символов. Текущая длина описания фильма "
                    + film.getDescription().length() + " символов.");
            return false;
        }
    }

    private boolean isCorrectReleaseDate() {//дата релиза — не раньше 28 декабря 1895 года;
        if(film.getReleaseDate().isAfter(dateFirstFilm)) {
            return true;
        } else {
            log.warn("Ошибка во входных данных фильма. Некорректная дата релиза " + film.getReleaseDate()
                    + ". Релиз должен быть позже " + dateFirstFilm);
            return false;
        }
    }

    private boolean isPositiveDuration() {//продолжительность фильма должна быть положительной.
        if(film.getDuration() > 0) {
            return true;
        } else {
            log.warn("Ошибка во входных данных фильма. Продолжительность фильма должна быть положительной.");
            return false;
        }
    }
}
