package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(getNextId());
        log.debug("Валидация пройдена.");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (users.containsKey(user.getId())) {
            user.setBirthday(user.getBirthday());
            user.setLogin(user.getLogin());
            user.setEmail(user.getEmail());
            if (user.getName() == null) {
                user.setName(user.getLogin());
                log.debug("Заменили имя на логин.");
            } else {
                user.setName(user.getName());
            }
            return user;
        } else throw new NotFoundException("Такого пользователя нет в списке!");
    }

    @Override
    public User createFriendship(long id, long friendId) {
        if (getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (getUserById(friendId) == null) {
            throw new NotFoundException("Невозможно добавить в друзья несуществующего юзера!");
        }
        getUserById(id).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(id);
        return getUserById(id);
    }

    @Override
    public User deleteFriendship(long id, long friendId) {
        if (getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (getUserById(friendId) == null) {
            throw new NotFoundException("Удаляемого из друзья юзера нет в списке!");
        }
        getUserById(id).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(id);
        return getUserById(id);
    }

    @Override
    public Collection<User> listOfFriends(long id) {
        if (getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (getUserById(id).getFriends() == null) {
            throw new NotFoundException("Список друзей пуст!");
        }
        return getUserById(id).getFriends().stream()
                .map(friends -> getUserById(friends))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> listOfCommonFriends(Long id, Long otherId) {
        if (getUserById(id) == null || getUserById(otherId) == null) {
            throw new NotFoundException("Одного из юзеров нет в списке!");
        }
        if (getUserById(id).getFriends() == null || getUserById(otherId).getFriends() == null) {
            throw new NotFoundException("Один из списков друзей пуст!");
        }
        getUserById(id).getFriends().retainAll(getUserById(otherId).getFriends());
        return getUserById(id).getFriends().stream()
                .map(friends -> getUserById(friends))
                .collect(Collectors.toList());
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
