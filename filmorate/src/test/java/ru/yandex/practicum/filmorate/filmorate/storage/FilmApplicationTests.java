package ru.yandex.practicum.filmorate.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.filmorate.model.Film;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmApplicationTests {
    private final FilmStorage filmStorage;

    @Test
    public void testFindUserById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1));
        AssertionsForClassTypes.assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateUser() {
        Film film = new Film("name",
                "dest",
                LocalDate.of(1974,10,2),
                140,
                null,
                null);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(film));
        AssertionsForClassTypes.assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        AssertionsForClassTypes.assertThat(film1).hasFieldOrPropertyWithValue("film_id", 2)
                );
    }

    @Test
    public void testUpdateUser() {
        Film film = new Film(
                1,
                "name",
                "dest",
                LocalDate.of(1974,10,2),
                140,
                null,
                null);
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.update(film));
        AssertionsForClassTypes.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        AssertionsForClassTypes.assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
    }
}