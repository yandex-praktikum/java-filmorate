package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    Film testFilm;

    @BeforeEach
    void setUp() {
        testFilm = Film.builder()
            .name("test name")
            .description("test description")
            .duration(60)
            .releaseDate(LocalDate.of(2001, 12, 12))
            .build();
    }

    @Test
    void addFilm() throws Exception {
        mockMvc.perform(
                post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testFilm))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("test name"));
    }

    @Test
    void addFilmWithEmptyName() throws Exception {
        testFilm.setName("");

        mockMvc.perform(post("/films")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testFilm))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void addFilmWithOverDescription() throws Exception {
        testFilm.setDescription("Невероятно интересный фильм, поднимающий животрепещущую тему современного мира, " +
            "который не может быть добавлен в написанное приложение из-за избыточного количества символов: " +
            "а именно, в количестве более двухсот.");

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFilm))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void addFilmWithZeroDuration() throws Exception {
        testFilm.setDuration(0);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFilm))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void addFilmWithOldDate() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(1890, 12, 21));

        NestedServletException thrownException = assertThrows(
            NestedServletException.class,
            () -> mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFilm))
            )
        );

        assertEquals(FilmValidationException.class, thrownException.getCause().getClass());
    }

    @Test
    void addFilmWithNegativeDuration() throws Exception {
        testFilm.setDuration(-100);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFilm))
            )
            .andExpect(status().isBadRequest());
    }
}