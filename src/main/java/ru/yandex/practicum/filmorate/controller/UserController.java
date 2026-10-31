package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        List<User> userList = new ArrayList<>(users.values());
        log.info("Возвращено {} пользователей", userList.size());
        return userList;
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);

        try {
            UserValidator.validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                String oldName = user.getName();
                user.setName(user.getLogin());
                log.debug("Имя пользователя не указано, использован логин: {}", user.getLogin());
            }
            user.setId(currentId++);
            users.put(user.getId(), user);
            log.info("Пользователь успешно создан с id: {}, логин: {}", user.getId(), user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        try {
            if (user.getId() == 0) {
                log.warn("Попытка обновить пользователя без указания ID");
                throw new ValidationException("Id должен быть указан");
            }

            if (!users.containsKey(user.getId())) {
                log.warn("Пользователь с id {} не найден для обновления", user.getId());
                throw new ValidationException("Пользователь с id " + user.getId() + " не найден");
            }
            UserValidator.validate(user);

            if (user.getName() == null || user.getName().isBlank()) {
                String oldName = user.getName();
                user.setName(user.getLogin());
                log.debug("Имя пользователя не указано, использован логин: {}", user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлен с id: {}, логин: {}", user.getId(), user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя с id {}: {}",
                    user.getId(), e.getMessage());
            throw e;
        }
    }
}
