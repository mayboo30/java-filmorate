package validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("Валидация корректного пользователя проходит успешно")
    void validateValidUserShouldPass() {
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Email не может быть null")
    void validateUserWithNullEmailShouldThrowException() {
        user.setEmail(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Email не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Email не может быть пустой строкой")
    void validateUserWithEmptyEmailShouldThrowException() {
        user.setEmail("");
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Email не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Email должен содержать символ @")
    void validateUserWithEmailWithoutAtShouldThrowException() {
        user.setEmail("testexample.com");
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Email должен содержать символ @", exception.getMessage());
    }

    @Test
    @DisplayName("Email с @ проходит валидацию")
    void validateUserWithValidEmailShouldPass() {
        user.setEmail("valid@email.com");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Логин не может быть null")
    void validateUserWithNullLoginShouldThrowException() {
        user.setLogin(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Логин не может быть пустой строкой")
    void validateUserWithEmptyLoginShouldThrowException() {
        user.setLogin("");
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Логин не может содержать пробелы")
    void validateUserWithLoginContainingSpacesShouldThrowException() {
        user.setLogin("test user");
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    @DisplayName("Логин без пробелов проходит валидацию")
    void validateUserWithLoginWithoutSpacesShouldPass() {
        user.setLogin("testuser");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Null имя проходит валидацию")
    void validateUserWithNullNameShouldPass() {
        user.setName(null);
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Пустое имя проходит валидацию")
    void validateUserWithEmptyNameShouldPass() {
        user.setName("");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Дата рождения в прошлом проходит валидацию")
    void validateUserWithPastBirthdayShouldPass() {
        user.setBirthday(LocalDate.of(2000, 1, 1));
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Дата рождения сегодня проходит валидацию")
    void validateUserWithTodayBirthdayShouldPass() {
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    @DisplayName("Дата рождения в будущем вызывает ошибку")
    void validateUserWithFutureBirthdayShouldThrowException() {
        user.setBirthday(LocalDate.of(2030, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> UserValidator.validate(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Null дата рождения проходит валидацию")
    void validateUserWithNullBirthdayShouldPass() {
        user.setBirthday(null);
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }
}