package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    private User validUser;
    private BindingResult bindingResult;

    @BeforeEach
    public void setup() {
        openMocks(this);
        validUser = new User();
        validUser.setId(1L);
        validUser.setEmail("test@example.com");
        validUser.setLogin("testlogin");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));
        bindingResult = new BeanPropertyBindingResult(validUser, "user");
    }


    @Test
    public void whenUpdateUserWithBindingErrors_thenBadRequest() {
        // Simulate binding errors
        bindingResult.rejectValue("email", "Error", "Email format is invalid");

        ResponseEntity<Object> response = userController.updateUser(validUser.getId(), validUser, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Validation error"));
    }

    @Test
    public void whenCreateUserWithInvalidBirthDate_thenInternalError() {
        validUser.setBirthday(LocalDate.now().plusDays(1)); // Invalid future date

        ResponseEntity<Object> response = userController.createUser(validUser, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error").toString().contains("Error creating user due to invalid input"));
    }

    @Test
    public void whenUpdateUserWithFutureBirthday_thenThrowValidationException() {
        validUser.setBirthday(LocalDate.now().plusDays(1)); // Future birthday

        ResponseEntity<Object> response = userController.updateUser(validUser.getId(), validUser, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("Дата рождения не может быть в будущем."));
    }
    @Test
    public void whenUserNotFound_thenResponseNotFound() {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(null);

        ResponseEntity<Object> response = userController.updateUser(validUser.getId(), validUser, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>) Objects.requireNonNull(response.getBody())).get("error")
                .toString().contains("User not found with ID"));
    }

    @Test
    public void whenValidUpdate_thenUserIsUpdatedSuccessfully() {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(validUser);

        ResponseEntity<Object> response = userController.updateUser(validUser.getId(), validUser, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService).updateUser(validUser.getId(), validUser);
    }

    @Test
    public void whenUpdateUserWithEmptyName_thenDisplayNameIsSetToLogin() {
        validUser.setName("");

        userController.updateUser(validUser.getId(), validUser, bindingResult);

        assertEquals(validUser.getLogin(), validUser.getName());
    }


    @Test
    public void whenCreateUserWithValidData_thenCreateUser() {
        User user = new User(1L, "email@example.com", "username", "Name",
                LocalDate.of(1990, 1, 1));
        User createdUser = new User(1L, "email@example.com", "username", "Name",
                LocalDate.of(1990, 1, 1));
        createdUser.setId(1L);

        BindingResult bindingResult = new BeanPropertyBindingResult(user, "user");
        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        ResponseEntity<Object> response = userController.createUser(user, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void whenCreateUserWithBindingErrors_thenBadRequest() {
        User user = new User(1L, "invalidemail", "username", "",
                LocalDate.of(1990, 1, 1));
        BindingResult bindingResult = new BeanPropertyBindingResult(user, "user");
        bindingResult.rejectValue("email", "Error", "Email should be valid");

        ResponseEntity<Object> response = userController.createUser(user, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenGetAllUsers_thenAllUsersReturned() {
        List<User> users = List.of(new User(1L, "email@example.com", "username",
                "Name", LocalDate.of(1990, 1, 1)));
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

}