package ru.yandex.practicum.filmorate.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(UserDbStorage.class)
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class UserDbStorageFriendshipTest {

    @Autowired
    private UserDbStorage userStorage;

    private User u1;
    private User u2;
    private User u3;

    private static User user(String email, String login, String name, LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    @BeforeEach
    void initUsers() {
        u1 = userStorage.create(user("a@example.com", "loginA", "User A", LocalDate.of(1990, 1, 1)));
        u2 = userStorage.create(user("b@example.com", "loginB", "User B", LocalDate.of(1991, 2, 2)));
        u3 = userStorage.create(user("c@example.com", "loginC", "User C", LocalDate.of(1992, 3, 3)));
    }

    @Test
    void addFriend_createsLink_andIsIdempotent() {
        userStorage.addFriend(u1.getId(), u2.getId());
        userStorage.addFriend(u1.getId(), u2.getId());

        Collection<User> friends = userStorage.findFriends(u1.getId());
        assertThat(friends)
                .hasSize(1)
                .first()
                .extracting(User::getId)
                .isEqualTo(u2.getId());
    }

    @Test
    void removeFriend_deletesLink() {
        userStorage.addFriend(u1.getId(), u2.getId());

        userStorage.removeFriend(u1.getId(), u2.getId());

        assertThat(userStorage.findFriends(u1.getId())).isEmpty();
    }

    @Test
    void findFriends_returnsDirectFriends() {
        userStorage.addFriend(u1.getId(), u2.getId());
        userStorage.addFriend(u1.getId(), u3.getId());

        Collection<User> friends = userStorage.findFriends(u1.getId());
        assertThat(friends).extracting(User::getId)
                .containsExactlyInAnyOrder(u2.getId(), u3.getId());
    }

    @Test
    void findMutualFriends_returnsIntersection() {
        userStorage.addFriend(u1.getId(), u2.getId());
        userStorage.addFriend(u1.getId(), u3.getId());
        userStorage.addFriend(u2.getId(), u3.getId());

        Collection<User> mutual = userStorage.findMutualFriends(u1.getId(), u2.getId());
        assertThat(mutual).singleElement()
                .extracting(User::getId)
                .isEqualTo(u3.getId());
    }
}
