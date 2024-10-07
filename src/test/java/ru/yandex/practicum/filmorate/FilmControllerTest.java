package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBadRequestForEmptyName() throws Exception {
        String filmJson = "{\"name\": \"\", \"description\": \"Valid description\", \"releaseDate\": \"2000-01-01\", \"duration\": 120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Название фильма не может быть пустым."));
    }

    @Test
    void shouldReturnBadRequestForTooLongDescription() throws Exception {
        String filmJson = "{\"name\": \"Valid Name\", \"description\": \"" + "a".repeat(201) + "\", \"releaseDate\": \"2000-01-01\", \"duration\": 120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Максимальная длина описания — 200 символов."));
    }

    @Test
    void shouldReturnBadRequestForNegativeDuration() throws Exception {
        String filmJson = "{\"name\": \"Valid Name\", \"description\": \"Valid description\", \"releaseDate\": \"2000-01-01\", \"duration\": -120}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Продолжительность фильма должна быть положительным числом."));
    }

    @Test
    void shouldReturnBadRequestForEmptyRequest() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}