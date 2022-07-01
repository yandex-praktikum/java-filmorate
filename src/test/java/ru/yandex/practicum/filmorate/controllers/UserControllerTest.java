package ru.yandex.practicum.filmorate.controllers;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
   @Autowired
    private MockMvc mockMvc;
   @Autowired
   private ObjectMapper objectMapper;

   private User user;

   @Test
    public void postWhenValidInput_thenReturnsUser() throws Exception {
       user = new User("test@gmail.com", "TestLogin", LocalDate.of(1993, 4, 20));
       user.setName("TestName");
       user.generateId();

      MvcResult mvcResult = mockMvc.perform(post("/users", 42L)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isOk())
              .andReturn();

       String actualResponseBody = mvcResult.getResponse().getContentAsString();
       String expectedResponseBody = objectMapper.writeValueAsString(user);
       assertEquals(expectedResponseBody, actualResponseBody);

   }

   @Test
    public void whenFailLogin_ReturnBadRequest() throws Exception {
       user = new User("test@gmail.com", "", LocalDate.of(1993, 4, 20));

       MvcResult mvcResult = mockMvc.perform(post("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isBadRequest())
               .andReturn();
   }

   @Test
    public void whenFailEmail_thenReturnBadRequest() throws Exception {
       user = new User("mail.ru", "TestLogin", LocalDate.of(1993, 4, 20));

       MvcResult mvcResult = mockMvc.perform(post("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isBadRequest())
               .andReturn();
   }

   @Test
   public void whenFailBirthday_thenReturnBadRequest() throws Exception {
       user = new User("test@gmail.com", "TestLogin", LocalDate.of(2100, 10, 16));

       MvcResult mvcResult = mockMvc.perform(post("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isBadRequest())
               .andReturn();
   }

   @Test
   public void whenEmptyName_thenUseLogin() throws Exception {
       user = new User("test@gmail.com", "TestLogin", LocalDate.of(1999, 5, 15));
       user.generateId();

       MvcResult mvcResult = mockMvc.perform(post("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andReturn();

       user.setName(user.getLogin());
       String actualResponseBody = mvcResult.getResponse().getContentAsString();
       String expectedResponseBody = objectMapper.writeValueAsString(user);
       assertEquals(expectedResponseBody, actualResponseBody);
   }

   @Test
    public void putWhenValidInput_thenReturnsUser() throws Exception {
       user = new User("test@mail.ru", "TestLogin", LocalDate.of(1991, 4, 20));
       user.setName("TestName");
       user.generateId();

       User updatedUser = new User("test@yandex.ru", "UpdatedLogin", LocalDate.of(1995, 4, 20));
       updatedUser.setName("UpdatedName");
       updatedUser.setId(user.getId());

        mockMvc.perform(post("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andReturn();


       MvcResult mvcResult = mockMvc.perform(put("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andReturn();

       String actualResponseBody = mvcResult.getResponse().getContentAsString();
       String expectedResponseBody = objectMapper.writeValueAsString(updatedUser);
       assertEquals(expectedResponseBody, actualResponseBody);
   }

   @Test
    public void putWhenUserNotExist_thenReturnBadRequest() throws Exception {
       user = new User("test@mail.ru", "TestLogin", LocalDate.of(1991, 4, 20));
       mockMvc.perform(put("/users", 42L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isInternalServerError())
               .andReturn();
   }
}
