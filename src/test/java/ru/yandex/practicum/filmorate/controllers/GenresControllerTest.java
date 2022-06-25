package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenresControllerTest {

    private final FilmDbStorage filmDbStorage;

    @Test
    void getAllGenres() {
        List<Genre> genresList = filmDbStorage.getAllGenres();
        assertEquals(6, genresList.size());
    }

    @Test
    void getGenre() {
        Optional<Genre> genre = Optional.of(filmDbStorage.getGenre(2L));
        assertThat(genre).isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 2L))
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("name", "Драма"));
    }

    @Test
    void emptyTest() {

    }
}