package ru.yandex.practicum.filmorate.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    @PostConstruct
    void which() {
        log.info("UserStorage bean = {}", userStorage.getClass().getName());
    }

    @Transactional
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User name is blank, fallback to login='{}'", user.getLogin());
        }
        User created = userStorage.create(user);
        log.info("User created: id={}, login='{}'", created.getId(), created.getLogin());
        return created;
    }

    @Transactional
    public User update(User user) {
        ensureUserExists(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User name is blank on update, fallback to login='{}'", user.getLogin());
        }
        User updated = userStorage.update(user);
        log.info("User updated: id={}", updated.getId());
        return updated;
    }

    public User findById(long id) {
        User user = ensureUserExists(id);
        log.debug("User fetched: id={}", id);
        return user;
    }

    public Collection<User> findAll() {
        Collection<User> all = userStorage.findAll();
        log.info("Get all users: count={}", all.size());
        return all;
    }

    public void deleteById(long id) {
        userStorage.deleteById(id);
        log.info("User deleted: id={}", id);
    }

    @Transactional
    public User addFriends(long userId, long friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("Cannot add yourself as a friend");
        }
        log.info("===Adding friends to friends list: id={}", userId);
        ensureUserExists(userId);
        ensureUserExists(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    @Transactional
    public User removeFriends(long userId, long friendId) {
        log.info("===Removing friends from friends list: id={}", userId);
        ensureUserExists(userId);
        ensureUserExists(friendId);
        return userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> findFriends(long userId) {
        ensureUserExists(userId);
        return userStorage.findFriends(userId);
    }

    public Collection<User> findMutualFriends(long userId, long anotherUserId) {
        ensureUserExists(userId);
        return userStorage.findMutualFriends(userId, anotherUserId);
    }

    private User ensureUserExists(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
