package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("validObjectFactory")
    void userValidationOkTest(User user) throws Exception {
        this.mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidObjectFactory")
    void userValidationNotOkTest(User user) throws Exception {
        this.mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    static Stream<User> validObjectFactory() {
        return Stream.of(
                new User(null, "mail@mail.ru", "logy", "Bob", LocalDate.of(1999,3,23)),
                new User(null, "mail@mail.ru", "Empty_name", "", LocalDate.of(1999,3,23))
        );
    }

    static Stream<User> invalidObjectFactory() {
        return Stream.of(
                new User(null, "", "login", "Empty_mail", LocalDate.of(1999,3,23)),
                new User(null, "mail!mail.ru", "login", "Mail_without_@", LocalDate.of(1999,3,23)),
                new User(null, "mail@mail.ru", "", "Empty_login", LocalDate.of(1999,3,23)),
                new User(null, "mail@mail.ru", "Lo Gy", "Login_with_space", LocalDate.of(1999,3,23)),
                new User(null, "mail@mail.ru", "login", "In_future", LocalDate.of(2999,3,23))
        );
    }
}