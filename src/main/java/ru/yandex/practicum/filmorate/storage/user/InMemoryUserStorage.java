package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private Integer id = 0;

    private Integer createId() {
        return ++id;
    }

    public boolean doesIdExist(int id) {
        return users.containsKey(id);
    }

    public User getUser(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.info("Пользователя с таким id нет");
            throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
        }
    }

    public List<User> getFriends(int id) {
        if (!users.isEmpty()) {
            if (users.containsKey(id)) {
                List<User> friends = new ArrayList<>();
                for (Integer i : users.get(id).getFriends()) {
                    friends.add(users.get(i));
                }
                return friends;
            } else {
                log.info("Пользователя с таким id нет");
                throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Пользователя с таким id нет");
            throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
        }
    }

    public User addFriend(int id, int friendId) {
        if (users.containsKey(id) && users.containsKey(friendId)) {
            users.get(id).getFriends().add(friendId);
            users.get(friendId).getFriends().add(id);
            log.info("Пользователь с id:" + friendId + " успешно добавлен в друзья");
            return users.get(id);
        } else {
            log.info("Пользователя с таким id нет");
            throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
        }
    }


    public User deleteFriend(int id, int friendId) {
        if (!users.isEmpty()) {
            if (users.containsKey(id) && users.containsKey(friendId)) {
                if (!users.get(id).getFriends().isEmpty() && !users.get(friendId).getFriends().isEmpty()) {
                    users.get(id).getFriends().remove(friendId);
                    users.get(friendId).getFriends().remove(id);
                    log.info("Пользователь с id:" + friendId + " успешно удален из друзей");
                    return users.get(id);
                } else {
                    log.info("Пользователя с таким id нет");
                    throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
                }
            } else {
                log.info("Пользователя с таким id нет");
                throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Пользователя с таким id нет");
            throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
        }
    }

    public List<User> getCommonFriends(int id, int otherId) {
        if (users.containsKey(id) && users.containsKey(otherId)) {
            if (!users.get(id).getFriends().isEmpty() && !users.get(otherId).getFriends().isEmpty()) {
                List<User> friends = new ArrayList<>();
                for (Integer i : users.get(id).getFriends()) {
                    i = Integer.parseInt(String.valueOf(i));
                    for (Integer ii : users.get(otherId).getFriends()) {
                        if (i.equals(ii)) {
                            friends.add(users.get(i));
                        }
                    }
                }
                if (friends.isEmpty()) {
                    log.info("Общих друзей нет");
                    return new ArrayList<>();
                } else {
                    return friends;
                }
            } else {
                log.info("Общих друзей нет");
                return new ArrayList<>();
            }
        } else {
            log.info("Одного из пользователей с таким id нет");
            return new ArrayList<>();
        }

    }


    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(createId());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь" + user.getName() + " добавлен", HttpStatus.CREATED);
        return user;
    }


    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            if (!users.containsKey(user.getId())) {
                log.info("Пользователя с таким id нет");
                throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
            } else {
                if (user.getFriends() == null) {
                    user.setFriends(new HashSet<>());
                }
                users.put(user.getId(), user);
                log.info("Пользователь {} обновлён", user.getName());
                return user;
            }
        } else if (!user.getName().isBlank()) {
            if (!users.containsKey(user.getId())) {
                log.info("Пользователя с таким id нет");
                throw new ValidException("Пользователя с таким id нет", HttpStatus.NOT_FOUND);
            } else {
                if (user.getFriends() == null) {
                    user.setFriends(new HashSet<>());
                }
                users.put(user.getId(), user);
                log.info("Пользователь {} обновлён", user.getName());
                return user;
            }
        } else {
            throw new ValidException("В имени присутствуют запрещённые символы", HttpStatus.BAD_REQUEST);
        }
    }

    public List<User> getALlUsers() {
        return new ArrayList<>(users.values());
    }
}
