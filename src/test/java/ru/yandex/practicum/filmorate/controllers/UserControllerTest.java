package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final UserDbStorage userDbStorage;
    private final FriendsStorage friendsStorage;

    Gson gson = new Gson();

    User user1 = gson.fromJson("{\"login\": \"loginUser1\", " +
                    "\"name\": \"nameUser1\", " +
                    "\"email\": \"user1@mail.ru\"," +
                    "\"birthday\": {\"year\":1955,\"month\":6,\"day\":6} }",
                    User.class );
    User user1_update = new User(1L,
            "user1@mail.ru",
            "login_update",
            "nameUser1",
            LocalDate.of(1955,6,6));
    User user2 = gson.fromJson("{\"login\": \"nameUser2\", " +
                    "\"name\": \"nameUser2\", " +
                    "\"email\": \"user2@mail.ru\"," +
                    "\"birthday\": {\"year\":1965,\"month\":5,\"day\":5} }",
                    User.class );
    User user3 = gson.fromJson("{\"login\": \"nameUser3\", " +
                    "\"name\": \"nameUser3\", " +
                    "\"email\": \"user3@mail.ru\"," +
                    "\"birthday\": {\"year\":1945,\"month\":4,\"day\":4} }",
                    User.class );


    @Test
    void userControllerTest() {
        userDbStorage.create(user1);
        Optional<User> userOptional = Optional.of(userDbStorage.getUser(1L));
        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));

        //update()
        userDbStorage.update(user1_update);
        Optional<User> userOptionalUpdate = Optional.of(userDbStorage.getUser(1L));
        assertThat(userOptionalUpdate).isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login_update"));
        //friendsTest
        friendsStorage.addFriend(1L, 2L);
        assertEquals(1,friendsStorage.getFriends(1L).size());
        assertEquals(0,friendsStorage.getFriends(2L).size());
        friendsStorage.removeFriend(1L, 2L);
        assertEquals(0,friendsStorage.getFriends(1L).size());
        //getCommonFriends()
        userDbStorage.create(user3);
        friendsStorage.addFriend(1L, 3L);
        friendsStorage.addFriend(2L, 3L);
        List<User> friendsList = friendsStorage.getCommonFriends(1L, 2L);
        assertEquals(1, friendsList.size());
        assertEquals(3L, friendsList.get(0).getId());
    }
}