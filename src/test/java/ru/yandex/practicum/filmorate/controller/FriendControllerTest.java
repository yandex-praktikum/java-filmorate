package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FriendControllerTest {

    private static final LocalDate BIRTHDAY = LocalDate.now().minusYears(20);
    private static final User VALID_USER = new User(1, "1@mail.ru", "login", "name", BIRTHDAY);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Проверка добавления в друзья
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOfTwoUsersOnPutFriends() throws Exception {
        //given
        postValidUser();
        postValidUser();

        // when
        mockMvc.perform(
                        put("/users/1/friends/2")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].friends.length()").value(1))
                .andExpect(jsonPath("$[1].friends.length()").value(1))
                .andExpect(jsonPath("$[0].friends[0]").value(2))
                .andExpect(jsonPath("$[1].friends[0]").value(1));
    }

    // Проверка удаления из друзей
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOfTwoUsersOnDeleteFriends() throws Exception {
        //given
        postValidUser();
        postValidUser();
        mockMvc.perform(
                put("/users/1/friends/2")
        );

        // when
        mockMvc.perform(
                        delete("/users/1/friends/2")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].friends.length()").value(0))
                .andExpect(jsonPath("$[1].friends.length()").value(0));
    }

    // Проверка получения списка друзей
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOfOneUserOnGetFriends() throws Exception {
        //given
        postValidUser();
        postValidUser();
        mockMvc.perform(
                put("/users/1/friends/2")
        );

        // when
        mockMvc.perform(
                        get("/users/1/friends")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    // Проверка получения списка общих друзей
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOfOneUserOnGetCommonFriends() throws Exception {
        //given
        postValidUser();
        postValidUser();
        postValidUser();
        mockMvc.perform(
                put("/users/1/friends/3")
        );
        mockMvc.perform(
                put("/users/2/friends/3")
        );

        // when
        mockMvc.perform(
                        get("/users/1/friends/common/2")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3));
    }

    private void postValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(VALID_USER))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
