package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerGetRequestTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Film film;

    @BeforeAll
    public void whenGetRequest_ReturnUsersList() throws Exception {
        film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 24), 100);
        film.generateId();

        List<Film> films = new ArrayList<>();
        films.add(film);

        mockMvc.perform(post("/films", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/films", 42L))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(films);
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
