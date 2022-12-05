package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.IncorrectParameterException;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

    public Optional<User> createUser(User user) {
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidateException("пустой логин или содержит пробелы");
        }

        if (user.getName().equals("") || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            throw new ValidateException("неправильный формат email или пустой email");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("дата рождения указывает на будущее время");
        }


        user.setId(getNewId());
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    public Optional<User> updateUser(User user) {
        if (user.getId() <= 0 || user.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        for (Long id : users.keySet()) {
            list.add(users.get(id));
        }
        return list;
    }

    @Override
    public void deleteUser(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("user not found");
        }
        users.remove(id);
    }

    public void deleteAll() {
        users.clear();
    }

    public Optional<User> getById(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("user not found");
        }
        return Optional.of(users.get(id));
    }
    @Override
    public void addFriend(Long id, Long idFriend) {
        if (users.get(id) == null || users.get(idFriend) == null) {
            throw new NotFoundException("user not found");
        }
        users.get(id).addFriend(idFriend);
        users.get(idFriend).addFriend(id);

    }

    @Override
    public void deleteFriend(Long id, Long idFriend) {
        if (users.get(id) == null) {
            throw new NotFoundException("user not found");
        }
        User user = users.get(id);
        user.removeFriend(idFriend);
    }

    @Override
    public List<User> getFriends(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("user not found");
        }
        List<User> user = new ArrayList<>();
        for(Long i: users.get(id).getFriends()) {
            user.add(users.get(i));
        }
        return user;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long idFriend) {
        if (users.get(id) == null || users.get(idFriend) == null) {
            throw new NotFoundException("user not found");
        }
        List<User> user = new ArrayList<>();
        for(Long i: users.get(id).getFriends()) {
            for(Long j: users.get(idFriend).getFriends()) {
                if (i == j) {
                    user.add(users.get(i));
                }
            }
        }
        return user;
    }
}
