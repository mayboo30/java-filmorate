package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public User add(User user) {
        log.info("Добавление пользователя: {}", user);
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя не указано, использован логин: {}", user.getLogin());
        }
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен с id: {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Обновление пользователя: {}", user);
        if (user.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя не указано, использован логин: {}", user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен с id: {}", user.getId());
        return user;
    }

    @Override
    public User delete(int id) {
        log.info("Удаление пользователя с id: {}", id);
        User removed = users.remove(id);
        if (removed == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return removed;
    }

    @Override
    public User getById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean existsById(int id) {
        return users.containsKey(id);
    }
}