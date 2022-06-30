package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerGetRequestTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeAll
    public void whenGetRequest_ReturnUsersList() throws Exception {
        user = new User("test@gmail.com", "TestLogin", LocalDate.of(1993, 4, 20));
        user.setName("TestName");
        user.generateId();

        List<User> users = new ArrayList<>();
        users.add(user);

        mockMvc.perform(post("/users", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/users", 42L))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(users);
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
