package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserController controller;

    @Test
    public void test_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"Ivan\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_emptyLoginShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_blankLoginShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \" \", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_emptyNameShouldReturnOkStatusAndPutLoginInName() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"John\", \"name\": \"\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(content().json("{\"id\": 0,\"email\": \"mail@mail.ru\"," +
                        "\"login\": \"John\", \"name\": \"John\"" +
                        ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_emptyEmailShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"\"," +
                                "\"login\": \"John\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_emailWithoutAtShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail_mail.ru\"," +
                                "\"login\": \"John\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_birthdayInFutureReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"John\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2025-02-02\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_addUserWithSameLoginAndEmailShouldReturn400CodeStatus() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"Jonathan\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"Jonathan\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"1920-02-02\"}"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"mail@yandex.ru\"," +
                                "\"login\": \"Jonathan\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"1920-02-02\"}"))
                .andExpect(status().isOk());
    }

    /*@Test
    public void test_updateUsersIdShouldBeInMap() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"email\": \"mail@mail.ru\"," +
                                "\"login\": \"John1\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 400,\"email\": \"mail@yandex.ru\"," +
                                "\"login\": \"John\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2000-02-02\"}"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"email\": \"mail@yandex.ru\"," +
                                "\"login\": \"John\", \"name\": \"Ivan\"" +
                                ", \"birthday\": \"2001-02-02\"}"))
                .andExpect(status().isOk());
    }*/
}
