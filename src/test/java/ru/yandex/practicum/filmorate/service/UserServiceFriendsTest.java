package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceFriendsTest {

    private UserStorage userStorage;
    private UserService userService;

    private long u1Id;
    private long u2Id;
    private long u3Id;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);

        u1Id = createUser("alice@example.com", "alice", "Alice", LocalDate.of(1990, 1, 1)).getId();
        u2Id = createUser("bob@example.com", "bob", "Bob", LocalDate.of(1991, 2, 2)).getId();
        u3Id = createUser("carol@example.com", "carol", "Carol", LocalDate.of(1992, 3, 3)).getId();

        assertThat(Set.of(u1Id, u2Id, u3Id)).doesNotContainNull();
        assertThat(u1Id).isNotEqualTo(u2Id).isNotEqualTo(u3Id);
    }

    private User createUser(String email, String login, String name, LocalDate birthday) {
        User u = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .friends(new HashSet<>())
                .build();
        return userStorage.create(u);
    }

    @Test
    void removeFriends_shouldRemoveSymmetrically_andBeIdempotent() {
        userService.addFriends(u1Id, u2Id);

        userService.removeFriends(u1Id, u2Id);

        assertThat(userService.findById(u1Id).getFriends()).doesNotContain(u2Id);
        assertThat(userService.findById(u2Id).getFriends()).doesNotContain(u1Id);

        userService.removeFriends(u1Id, u2Id);
        assertThat(userService.findById(u1Id).getFriends()).doesNotContain(u2Id);
    }

    @Test
    void findFriends_shouldReturnAllFriendsAsUsers() {
        userService.addFriends(u1Id, u2Id);
        userService.addFriends(u1Id, u3Id);

        Collection<User> friends = userService.findFriends(u1Id);

        assertThat(friends)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(u2Id, u3Id);
    }

    @Test
    void findMutualFriends_shouldReturnIntersection() {
        // u1 дружит с u2 и u3; u2 дружит только с u3 → общий друг = u3
        userService.addFriends(u1Id, u2Id);
        userService.addFriends(u1Id, u3Id);
        userService.addFriends(u2Id, u3Id);

        Collection<User> mutual = userService.findMutualFriends(u1Id, u2Id);

        assertThat(mutual)
                .extracting(User::getId)
                .containsExactly(u3Id);
    }

    @Test
    void addFriends_selfShouldThrowIllegalArgument() {
        assertThatThrownBy(() -> userService.addFriends(u1Id, u1Id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("(?i).*yourself.*");
    }

    @Test
    void findFriends_userNotFound_shouldThrowNotFound() {
        assertThatThrownBy(() -> userService.findFriends(4242L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findMutualFriends_userNotFound_shouldThrowNotFound() {
        assertThatThrownBy(() -> userService.findMutualFriends(u1Id, 4242L))
                .isInstanceOf(NotFoundException.class);
    }
}
