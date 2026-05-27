package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// Класс тестов контроллера пользователей
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Тест успешного создания корректного пользователя
    @Test
    void shouldCreateValidUser() throws Exception {
        User user = createUser();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("user@mail.ru"));
    }

    // Проверка использования login вместо пустого name
    @Test
    void shouldUseLoginAsNameWhenNameIsBlank() throws Exception {
        User user = createUser();
        user.setName(" ");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));
    }

    // Проверка ошибки при email без символа @
    @Test
    void shouldRejectEmailWithoutAtSymbol() throws Exception {
        User user = createUser();
        user.setEmail("wrong-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Проверка ошибки при пустом email
    @Test
    void shouldRejectBlankEmail() throws Exception {
        User user = createUser();
        user.setEmail(" ");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Проверка ошибки при пустом login
    @Test
    void shouldRejectBlankLogin() throws Exception {
        User user = createUser();
        user.setLogin(" ");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Проверка ошибки при login с пробелами
    @Test
    void shouldRejectLoginWithSpaces() throws Exception {
        User user = createUser();
        user.setLogin("do lore");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Проверка ошибки при дате рождения из будущего
    @Test
    void shouldRejectFutureBirthday() throws Exception {
        User user = createUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    // Проверка, что сегодняшняя дата допустима
    @Test
    void shouldAcceptTodayBirthday() throws Exception {
        User user = createUser();
        user.setBirthday(LocalDate.now());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    // Проверка ошибки при пустом теле запроса
    @Test
    void shouldRejectEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    // Проверка получения списка пользователей
    @Test
    void shouldReturnUsersList() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(0))));
    }

    // Вспомогательный метод для создания корректного пользователя
    private User createUser() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1990, 8, 20));
        return user;
    }
}
