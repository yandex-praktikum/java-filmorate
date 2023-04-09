package ru.yandex.practicum.filmorate.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_positive_film_adding() throws Exception {
        Film film = new Film();
        String filmAsString = objectMapper.writeValueAsString(film);
        String responseAsString = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmAsString)).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("У фильма нет названия", responseAsString);

        film.setName("title");
        film.setDescription("description");
        responseAsString = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Дата выхода фильма отсутствует", responseAsString);

        film.setReleaseDate(LocalDate.of(2023, 12, 28));
        responseAsString = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Продолжительность фильма должна быть больше нуля", responseAsString);

        film.setDuration(3000);
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film))).andExpect(status().is2xxSuccessful());

    }

    @Test
    public void test_positive_film_update() throws Exception {
        Film film = new Film();
        film.setId(125123);
        film.setName("title");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 12, 28));
        film.setDuration(3000);
        final String responseAsString = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Нет такого фильма", responseAsString);
        String newFilmAsString = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))).andExpect(status().isOk()).andReturn()
                .getResponse()
                .getContentAsString();

        Film newFilm = objectMapper.readValue(newFilmAsString, Film.class);
        newFilm.setDescription("New Description");
        mockMvc.perform(put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFilm))).andExpect(status().isOk());
    }
}