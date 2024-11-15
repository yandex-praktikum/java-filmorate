package ru.yandex.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.exception.NotFoundException;
import ru.yandex.model.Film;
import ru.yandex.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.yandex.model.User;

@Service
@RequiredArgsConstructor
@Getter
public class UserServiceImpl implements UserService {

    private final InMemoryUserStorage userStorage;
    public User createUser(User user) {
       return userStorage.createUser(user);
    }

    public User deleteUser(User user) {
       return userStorage.deleteUser(user);
    }

    public User updateUser(User newUser) {
       return userStorage.updateUser(newUser);
    }

    public Collection<User> allUsers() {
       return userStorage.allUsers();
    }

    public User addFriend(Long id, Long friendId) {
        if (userStorage.getUsers().containsKey(id)) {
            if (userStorage.getUsers().containsKey(friendId)) {
                User user = userStorage.getUsers().get(id);
                User friend = userStorage.getUsers().get(friendId);
                user.getFriends().add(friendId);
                friend.getFriends().add(id);
                return userStorage.getUsers().get(friendId);
            }
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    public User deleteFriend(Long id, Long friendId) {
        if (userStorage.getUsers().containsKey(id)) {
            if (userStorage.getUsers().containsKey(friendId)) {
                if (userStorage.getUsers().get(id).getFriends().contains(friendId) &&
                        userStorage.getUsers().get(friendId).getFriends().contains(id)) {
                    User user = userStorage.getUsers().get(id);
                    User friend = userStorage.getUsers().get(friendId);
                    user.getFriends().remove(friendId);
                    friend.getFriends().remove(id);
                    return userStorage.getUsers().get(friendId);
                }
                throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
            }
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
    }

    public Collection<Long> allFriends(Long id) {
        return userStorage.getUsers().get(id).getFriends();
    }

    public Collection<Long> generalFriends(Long id, Long otherId) {
        Set<Long> friends = userStorage.getUsers().get(id).getFriends().stream()
                .filter(x -> (userStorage.getUsers().get(otherId).getFriends()
                        .stream()
                        .anyMatch( y -> (y == x))
                ))
                .collect(Collectors.toSet());
        return friends;
    }

    public User getUserId(Long id) {
        return userStorage.getUsers().get(id);
    }
}
