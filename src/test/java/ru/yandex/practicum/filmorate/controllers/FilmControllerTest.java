package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Film film;

    @Test
    public void postWhenValidInput_thenReturnsFilm() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 24), 100);
        film.generateId();

        MvcResult mvcResult = mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(film);
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    public void postWhenFailName_thenReturnBadRequest() throws Exception {
        film = new Film("", "adipisicing", LocalDate.of(1967, 3, 24), 100);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void postWhenFailDescription_thenReturnBadRequest() throws Exception {
        film = new Film("nisi eiusmod", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь " +
                "они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. " +
                "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(1967, 3, 24), 100);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void postWhenFailReleaseDate_thenReturnBadRequest() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1895, 12, 27), 100);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void postWhenReleaseDateIsLimitValue_thenReturnIsOk() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1895, 12, 28), 100);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void postWhenFailDuration_thenReturnBadRequest() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1995, 10, 7), 0);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void postWhenDurationIsLimitValue_thenReturnIsOk() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1995, 10, 7), 1);
        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void putWhenValidInput_thenReturnsFilm() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 24), 100);
        film.generateId();

        Film updatedFilm = new Film("nisi eiusmod", "UpdateDescription", LocalDate.of(1967, 3, 24), 110);
        updatedFilm.setId(film.getId());

        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();


        MvcResult mvcResult = mockMvc.perform(put("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(updatedFilm);
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    public void putWhenFilmNotExist_thenReturnError() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 24), 100);
        mockMvc.perform(put("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}
