package ru.yandex.practicum.filmorate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
    LocalDate wrongUsersBirthday = LocalDate.now().plusDays(1L);
    LocalDate writeUsersBirthday = LocalDate.now().minusDays(1L);
    LocalDate secondBirthday = LocalDate.now().minusDays(365L);
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_positive_user_adding() throws Exception {
        User user = new User();
        String userAsString = objectMapper.writeValueAsString(user);
        String responseAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsString)).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", responseAsString);

        user.setEmail("an9262778931@yandex.ru");
        responseAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Логин не может быть пустым и содержать пробелы", responseAsString);

        user.setLogin("Zagloba");
        user.setName("Alex");
        user.setBirthday(wrongUsersBirthday);
        responseAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Дата рождения не может быть в будущем", responseAsString);

        user.setBirthday(writeUsersBirthday);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test_positive_user_update() throws Exception {
        User user = new User();
        user.setId(125678);
        user.setEmail("zagloba79@google.com");
        user.setLogin("Zagloba");
        user.setName("Alex");
        user.setBirthday(secondBirthday);

        final String responseAsString = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Нет такого пользователя", responseAsString);
        String newUserAsString = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andReturn()
                .getResponse()
                .getContentAsString();

        User newUser = objectMapper.readValue(newUserAsString, User.class);
        newUser.setLogin("Alexandr");
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))).andExpect(status().isOk());
    }
}