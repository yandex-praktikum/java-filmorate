package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final JdbcTemplate jdbcTemplate;

    Gson gson = new Gson();

    Film film1 = gson.fromJson("{\"name\": \"name1\", " +
                    "\"releaseDate\": {\"year\":1999,\"month\":5,\"day\":5}, " +
                    "\"description\": \"desc1\"," +
                    "\"duration\": 100," +
                    "\"mpa\": { \"id\": 2} }",
            Film.class);
    Film film1_update = new Film(1L,
            "name1_up",
            "desc1",
            LocalDate.of(1999, 6, 6),
            20,
            new RatingMPA(2L),
            null);
    Film film2 = gson.fromJson("{\"name\": \"name2\", " +
                    "\"releaseDate\": {\"year\":1998,\"month\":8,\"day\":8}, " +
                    "\"description\": \"desc2\"," +
                    "\"duration\": 200," +
                    "\"mpa\": { \"id\": 3} }",
            Film.class);
    User user = gson.fromJson("{\"login\": \"nameUser\", " +
                    "\"name\": \"nameUser\", " +
                    "\"email\": \"user@mail.ru\"," +
                    "\"birthday\": {\"year\":1955,\"month\":6,\"day\":6} }",
            User.class);

    @Test
    void filmControllerTest() {
        filmDbStorage.create(film1);
        Film film = filmDbStorage.getFilm(1L);
        assertEquals(1L, (long) film.getId());
        //getAllFilms()
        filmDbStorage.create(film2);
        List<Film> filmList = filmDbStorage.getAllFilms();
        assertEquals(2, filmList.size());
        //update()
        filmDbStorage.create(film1);
        filmDbStorage.update(film1_update);
        Optional<Film> filmOptional = Optional.of(filmDbStorage.getFilm(1L));
        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(filmVar ->
                        assertThat(filmVar).hasFieldOrPropertyWithValue("name", "name1_up"));
        //likesTest
        userDbStorage.create(user);
        likeDbStorage.addLike(2L, 1L);
        List<Film> filmListLikes = likeDbStorage.getFilmsWithMostLikes(1);
        assertEquals(1, filmListLikes.size());
        assertEquals(2L, (long) filmListLikes.get(0).getId());
        likeDbStorage.removeLike(2L, 1L);
        likeDbStorage.addLike(3L, 1L);
        filmListLikes = likeDbStorage.getFilmsWithMostLikes(2);
        assertEquals(2, filmListLikes.size());
        assertEquals(3L, (long) filmListLikes.get(0).getId());
    }
}