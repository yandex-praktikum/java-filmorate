package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaControllerTest {

    private final FilmDbStorage filmDbStorage;

    @Test
    void getAllMpa() {
        List<RatingMPA> ratingMPAList = filmDbStorage.getAllMpa();
        assertEquals(5, ratingMPAList.size());
    }

    @Test
    void getMpa() {
        Optional<RatingMPA> mpa = Optional.of(filmDbStorage.getMpa(2L));
        assertThat(mpa).isPresent()
                .hasValueSatisfying(rating ->
                assertThat(rating).hasFieldOrPropertyWithValue("id", 2L))
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("name", "PG"));
    }
}