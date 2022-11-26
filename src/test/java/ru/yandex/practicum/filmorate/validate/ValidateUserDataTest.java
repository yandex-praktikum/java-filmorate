package ru.yandex.practicum.filmorate.validate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidateUserDataTest {
    static User user = User.builder()
            .name("name")
            .email("email@yandex.ru")
            .login("login")
            .birthday(LocalDate.of(1994, 10, 01))
            .build();

    @AfterEach
    public void user() {
        user = User.builder()
                .name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.of(1994, 10, 01))
                .build();
    }

    @Test
    public void correctAllData() {
        assertTrue(new UserDataValidate(user).checkAllData());
    }

    @Test
    public void incorrectEmail() {
        user.setEmail("emailyandex.ru");
        assertFalse(new UserDataValidate(user).checkAllData());
    }

    @Test
    public void incorrectLogin() {
        user.setLogin("log in");
        assertFalse(new UserDataValidate(user).checkAllData());
    }

    @Test
    public void incorrectBirthday() {
        user.setBirthday(LocalDate.of(2025, 10, 01));
        assertFalse(new UserDataValidate(user).checkAllData());
    }
}
