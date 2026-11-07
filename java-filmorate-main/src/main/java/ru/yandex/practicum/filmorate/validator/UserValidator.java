package ru.yandex.practicum.filmorate.validator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validate(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Ошибка валидации пользователя: email пустой");
            throw new ValidationException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка валидации пользователя: email '{}' не содержит @", user.getEmail());
            throw new ValidationException("Email должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Ошибка валидации пользователя: логин пустой");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации пользователя: логин '{}' содержит пробелы", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации пользователя: дата рождения '{}' в будущем", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        log.debug("Пользователь прошел валидацию: {}", user.getLogin());

    }
}
