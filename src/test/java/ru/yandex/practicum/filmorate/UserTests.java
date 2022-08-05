package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.controllers.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserTests {
    @Test
    void LoginWithSpace(){
        UserController userController=new UserController();
        User user=new User("eee@mail.ru","Ivan K", LocalDate.of(2003,03,31));
        Throwable exception = Assertions.assertThrows(
                ValidationException.class,
                () -> {
                    userController.createUser(user);
                }
        );
        Assertions.assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

}
