package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FriendsController {

    private final FriendsService friendsService;

    @PutMapping("/{friendId}")
    public List<User> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        List<User> result = friendsService.addFriend(id, friendId);

        log.debug("User with id = {} is now a friend of user with id = {}.", id, friendId);
        return result;
    }

    @DeleteMapping("/{friendId}")
    public List<User> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        List<User> result = friendsService.deleteFriend(id, friendId);

        log.debug("User with id = {} is no longer a friend of user with id = {}.", id, friendId);
        return result;
    }

    @GetMapping
    public List<User> getFriends(@PathVariable Integer id) {
        List<User> result = friendsService.getFriends(id);

        log.debug("List of friends of user with id = {} is received with a length of {}.", id, result.size());
        return result;
    }

    @GetMapping("/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        List<User> result = friendsService.getCommonFriends(id, otherId);

        log.debug("List of common friends of user with id = {} with user with id = {} is received with a length of  {}.", id, otherId, result.size());
        return result;
    }
}
