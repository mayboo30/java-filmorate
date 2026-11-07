package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) {
        log.info("Добавление пользователя {} в друзья пользователя {}", friendId, userId);
        if (userId == friendId) {
            throw new ValidationException("Пользователь не может добавить в друзья себя же самого");
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи {} и {} теперь друзья!", userId, friendId);
        return user;
    }

    public User removeFriend(int userId, int friendId) {
        log.info("Удаление пользователя {} из друзей пользователя {}", friendId, userId);
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
        return user;
    }

    public List<User> getFriends(int userId) {
        log.info("Получение списка друзей пользователя {}", userId);
        User user = userStorage.getById(userId);
        return user.getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherId);
        User user = userStorage.getById(userId);
        User other = userStorage.getById(otherId);
        Set<Integer> commonIds = user.getFriends().stream().filter(other.getFriends()::contains).collect(Collectors.toSet());
        return commonIds.stream().map(userStorage::getById).collect(Collectors.toList());
    }
}