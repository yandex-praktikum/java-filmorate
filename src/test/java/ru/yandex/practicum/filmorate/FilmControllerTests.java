package ru.yandex.practicum.filmorate;

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
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FilmControllerTests {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void test1_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "nisi eiusmod"
                                 , LocalDate.of(1967, 3, 25), 100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isOk())
                                    .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test2_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(2, null, "nisi eiusmod"
                                 , LocalDate.of(1967, 3, 25), 100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [name]"));
        assertTrue(message.contains("default message [must not be null]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test3_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(3, "nisi eiusmod", null
                                 , LocalDate.of(1967, 3, 25), 100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [description]"));
        assertTrue(message.contains("default message [must not be null]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test4_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(4, "nisi eiusmod", "nisi eiusmod"
                                 , LocalDate.of(1667, 3, 25), 100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [releaseDate]"));
        assertTrue(message.contains("default message [Не верная дата релиза фильма.]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test5_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(5, "nisi eiusmod", "nisi eiusmod", null, 100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [releaseDate]"));
        assertTrue(message.contains("default message [must not be null]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test6_createValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(6, "nisi eiusmod", "nisi eiusmod"
                                 , LocalDate.of(1967, 3, 25), -100);
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [duration]"));
        assertTrue(message.contains("default message [must be greater than 0]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test7_createValidFilmResponseShouldBeOk() throws Exception {
        this.mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(" "))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                    .andReturn();
    }

    @Test
    void test1_updateValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(7, "nisi eiusmod", "nisi eiusmod"
                                 , LocalDate.of(1967, 3, 25), 1000);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        this.mockMvc.perform(put("/films")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

    }

    @Test
    void test2_updateValidFilmResponseShouldBeOk() throws Exception {
        Film film = new Film(7, "nisi eiusmod", "nisi eiusmod"
                , LocalDate.of(1967, 3, 25), 1000);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    void test3_updateValidFilmResponseShouldBeOk() throws Exception {
        this.mockMvc.perform(put("/films")
                        .content(" ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void test1_findValidFilmResponseShouldBeOk() throws Exception {
        Film film_1 = new Film(8, "nisi eiusmod", "nisi eiusmod"
                                   , LocalDate.of(1967, 3, 25), 100);
        Film film_2 = new Film(9, "nisi", "eiusmod"
                                   , LocalDate.of(1997, 3, 25), 200);
        String body = mapper.writeValueAsString(film_1);
        String body_1 = mapper.writeValueAsString(film_2);
        this.mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk());
        this.mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body_1))
                    .andExpect(status().isOk());
        MvcResult response = mockMvc.perform(get("/films"))
                                    .andExpect(status().isOk())
                                    .andReturn();
        String listFilms = response.getResponse().getContentAsString();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
        assertNotNull(listFilms, "Данные не вернулись");
    }
}
