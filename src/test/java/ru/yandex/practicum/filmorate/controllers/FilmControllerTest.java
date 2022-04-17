package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    FilmController filmController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        filmController.put(FilmUserTestData.film1);
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}]"));
    }

    @Test
    void createAllOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}"));
    }

    @Test
    void createBadData() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"1891-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createEmptyName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createTooLongDescription() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some long descriptionfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createBadDuration() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"P-T2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createNullDuration() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"ZERO\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void putAllOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}"));
    }

    @Test
    void putBadData() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"1891-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void putEmptyName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void putTooLongDescription() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some long descriptionfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void putBadDuration() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"P-T2H\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void putNullDuration() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films")
                        .content("{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"ZERO\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;
    }

    @AfterEach
    void delete() {
        filmController.getFilms().clear();
    }
}