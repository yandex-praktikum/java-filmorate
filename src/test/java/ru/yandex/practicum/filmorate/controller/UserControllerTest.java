package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1)
            .login("login")
            .email("test@mail.ru")
            .birthday(LocalDate.of(1990, 12, 12))
            .name("name")
            .build();
    }

    @Test
    void createUser() throws Exception {
        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void createUserWithBlankEmail() throws Exception {
        testUser.setEmail("");

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().is4xxClientError());
    }

    @Test
    void createUserWithIncorrectEmail() throws Exception {
        testUser.setEmail("testmail.ru");

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().is4xxClientError());
    }

    @Test
    void createUserWithIncorrectLogin() throws Exception {
        testUser.setEmail("test2@mail.ru");
        testUser.setLogin("");

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().is4xxClientError());
    }

    @Test
    void createUserWithBlankLogin() throws Exception {
        testUser.setEmail("test5@mail.ru");
        testUser.setLogin("dolore dunkan");

        NestedServletException thrownException = assertThrows(
            NestedServletException.class,
            () -> mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser))
            )
        );

        assertEquals(UserValidationException.class, thrownException.getCause().getClass());
    }

    @Test
    void createUserWithEmptyName() throws Exception {
        testUser.setEmail("test3@mail.ru");
        testUser.setName("");

        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("login"));
    }

    @Test
    void createUserWithIncorrectBirthday() throws Exception {
        testUser.setEmail("test4@mail.ru");
        testUser.setBirthday(LocalDate.of(3000, 12, 12));

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser))
            )
            .andExpect(status().is4xxClientError());
    }
}