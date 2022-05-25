package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(1, "mail@mail.ru", "login", "name"
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isOk())
                                    .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test2_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(2, "mail.ru", "login", "name"
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [must be a well-formed email address]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }
    @Test
    void test3_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(3, "mail@mail.ru", null, "name"
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [must not be blank]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test4_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(4, "mail@mail.ru", "login login", "name"
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [must match \"\\S*$\"]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test5_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(5, "mail@mail.ru", "Login", " "
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test6_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(6, "mail@mail.ru", "Login", " "
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void test7_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(7, "mail@mail.ru", "login", "name"
                                 , LocalDate.of(2967, 3, 25));
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [Не верная дата рождения.]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }
    @Test
    void test8_createValidUserResponseShouldBeOk() throws Exception {
        User user = new User(8, "mail@mail.ru", "login", "name", null);
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isBadRequest())
                                    .andReturn();

        String message = Objects.requireNonNull(response.getResolvedException()).getMessage();
        int statusCod = response.getResponse().getStatus();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [must not be null]"));
        assertEquals(400, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test1_updateValidUserResponseShouldBeOk() throws Exception {
        User user = new User(9, "mail@mail.ru", "login", "name"
                                 , LocalDate.of(1967, 3, 25));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        this.mockMvc.perform(put("/users")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        this.mockMvc.perform(put("/users")
                    .content(" ")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException));
    }

    @Test
    void test1_findValidUserResponseShouldBeOk() throws Exception {
        User user = new User(10, "mail@mail.ru", "login", "name"
                                 , LocalDate.of(1967, 3, 25));
        User user_1 = new User(11, "mail@mail.ru", "login", "name"
                                   , LocalDate.of(1987, 5, 15));
        String body = mapper.writeValueAsString(user);
        String body_1 = mapper.writeValueAsString(user_1);
        this.mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk());
        this.mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body_1))
                    .andExpect(status().isOk());
        MvcResult response = mockMvc.perform(get("/users"))
                                    .andExpect(status().isOk())
                                    .andReturn();
        String listUsers = response.getResponse().getContentAsString();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
        assertNotNull(listUsers, "Данные не вернулись");

    }
}