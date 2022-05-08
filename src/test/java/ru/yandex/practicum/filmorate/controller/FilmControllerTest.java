package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1967-03-25\", \"duration\": 90}"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_emptyNameShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1967-03-25\", \"duration\": 90}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_tooLongDescriptionShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"aaaaaaaaaaaaaaaaaaaaaaaaa      "
                                + "              ffffffffffffffffffffffffffffffffffffffffffffffffvvvvvvvvvvvvvvvvvvvvv"
                                + "vvvvvvvvvd78878sfowporewjroieweeeeeeeeeeeeeeeeeeeeee5432534534tffgggfhfhfhfghgfgfhf"
                                + "ghgfhg\","
                                + "\"releaseDate\": \"1967-03-25\", \"duration\": 90}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_releaseDateIsBeforeThanOldestFilmShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1895-12-27\", \"duration\": 90}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_negativeDurationShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1895-12-28\", \"duration\": -1}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_addFilmWithSameNameAndReleaseDateShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1985-12-28\", \"duration\": 100}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\", \"description\": \"film description\","
                                + "\"releaseDate\": \"1985-12-28\", \"duration\": 90}"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\", \"description\": \"film description\","
                                + "\"releaseDate\": \"2021-12-28\", \"duration\": 90}"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_updateFilmWithDifferentIdShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"once upon a time\",\"description\": \"film description\","
                                + "\"releaseDate\": \"1895-12-28\", \"duration\": 100}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"once upon a time\", "
                                + "\"description\": \"Different film description\","
                                + "\"releaseDate\": \"2000-12-28\", \"duration\": 90}"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 0, \"name\": \"once upon a time\", "
                                + "\"description\": \"Different film description\","
                                + "\"releaseDate\": \"2000-12-28\", \"duration\": 90}"))
                .andExpect(status().isOk());
    }
}