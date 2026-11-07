package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен запрос на получение пользователя с id: {}", id);
        return userStorage.getById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        return userStorage.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}