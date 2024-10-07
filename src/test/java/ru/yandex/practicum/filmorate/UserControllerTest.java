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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBadRequestForEmptyEmail() throws Exception {
        String userJson = "{\"email\": \"\", \"login\": \"validLogin\", \"name\": \"Valid Name\", \"birthday\": \"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Электронная почта не может быть пустой и должна содержать символ '@'."));
    }

    @Test
    void shouldReturnBadRequestForInvalidLogin() throws Exception {
        String userJson = "{\"email\": \"valid@test.com\", \"login\": \"invalid login\", \"name\": \"Valid Name\", \"birthday\": \"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Логин не может быть пустым и содержать пробелы."));
    }

    @Test
    void shouldReturnBadRequestForFutureBirthday() throws Exception {
        String userJson = "{\"email\": \"valid@test.com\", \"login\": \"validLogin\", \"name\": \"Valid Name\", \"birthday\": \"3000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Дата рождения не может быть в будущем."));
    }

    @Test
    void shouldReturnBadRequestForEmptyRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
