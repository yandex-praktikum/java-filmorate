package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private Integer globalId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Integer id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(globalId);

        users.put(globalId, user);
        globalId++;

        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> addFriend(Integer id, Integer friendId) {
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);

        return List.of(users.get(id), users.get(friendId));
    }

    @Override
    public List<User> deleteFriend(Integer id, Integer friendId) {
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);

        return List.of(users.get(id), users.get(friendId));
    }

    @Override
    public List<User> getFriends(Integer id) {
        return users.get(id).getFriends().stream().map(users::get).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return users.get(id).getFriends().stream().filter(users.get(otherId).getFriends()::contains).map(users::get).collect(Collectors.toList());
    }

    @Override
    public void checkUserExist(Integer id) {
        if (findById(id) == null) {
            throw new NotFoundException(String.format("User with id = %d is not found.", id));
        }
    }
}
