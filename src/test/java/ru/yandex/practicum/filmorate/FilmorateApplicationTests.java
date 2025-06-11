package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    static UserController uc;
    static FilmController fc;

    static User user1;
    static User user2;

    static Film film1;
    static Film film2;

    @BeforeAll
    static void create() {

        uc = new UserController();
        fc = new FilmController();

        user1 = new User();
        user2 = new User();

        user1.setName("user1Name");
        user1.setLogin("user1Login");
        user1.setEmail("user1@yandex.ru");
        user1.setBirthday(LocalDate.of(1995, 12, 1));

        user2.setName("user2Name");
        user2.setLogin("user2Login");
        user2.setEmail("user2@yandex.ru");
        user2.setBirthday(LocalDate.of(1996, 11, 1));

        film1 = new Film();
        film2 = new Film();

        film1.setName("Film1Name");
        film1.setDescription("Film1Description");
        film1.setDuration(120);
        film1.setReleaseDate(LocalDate.of(1965, 10, 5));

        film2.setName("Film2Name");
        film2.setDescription("Film2Description");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(1965, 10, 5));
    }

    @AfterEach
    void cleanCollections() {
        uc.getAllUsers().removeAll(uc.getAllUsers());
        fc.getAllFilms().removeAll(fc.getAllFilms());
    }

    @Test
    void shouldAddCorrectUser() {
        uc.addUser(user1);
        uc.addUser(user2);
        Assertions.assertEquals(2, uc.getAllUsers().size());
    }

    @Test
    void shouldUpdateCorrectUser() {

        uc.addUser(user1);
        uc.addUser(user2);

        User user3 = new User();

        user3.setName("user3Name");
        user3.setLogin("user3Login");
        user3.setEmail("user3@yandex.ru");
        user3.setId(1L);

        user3.setBirthday(LocalDate.of(1997, 12, 3));

        uc.updateUser(user3);

        User userInUsers = uc.users.get(user3.getId());

        System.out.println(uc.getAllUsers());

        Assertions.assertEquals("user3Name", userInUsers.getName());
    }


//	@Test
//	void shouldNotAddInvalidEmail() {
//		User user = new User();
//
//		user.setName("John");
//		user.setEmail("");
//		user.setLogin("Login");
//		user.setBirthday(LocalDate.of(1995, 10, 15));
//
//		uc.addUser(user);
//
//		Assertions.assertEquals(0, uc.getAllUsers().size());
//	}
}
