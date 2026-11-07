package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import java.time.Duration;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
    }

    @Test
    @DisplayName("Валидация корректного фильма проходит успешно")
    void validateValidFilmShouldPass() {
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Название не может быть null")
    void validateFilmWithNullNameShouldThrowException() {
        film.setName(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Название не может быть пустой строкой")
    void validateFilmWithEmptyNameShouldThrowException() {
        film.setName("");
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Название не может состоять только из пробелов")
    void validateFilmWithBlankNameShouldThrowException() {
        film.setName("   ");
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Описание длиной ровно 200 символов проходит валидацию")
    void validateFilmWith200CharactersDescriptionShouldPass() {
        String description = "a".repeat(200);
        film.setDescription(description);
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Описание длиной 201 символ вызывает ошибку")
    void validateFilmWith201CharactersDescriptionShouldThrowException() {
        String description = "a".repeat(201);
        film.setDescription(description);
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Максимальная длина описания - 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Null описание проходит валидацию")
    void validateFilmWithNullDescriptionShouldPass() {
        film.setDescription(null);
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Пустое описание проходит валидацию")
    void validateFilmWithEmptyDescriptionShouldPass() {
        film.setDescription("");
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Дата релиза 28 декабря 1895 года проходит валидацию")
    void validateFilmWithMinReleaseDateShouldPass() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Дата релиза 27 декабря 1895 года вызывает ошибку")
    void validateFilmWithDateBeforeMinShouldThrowException() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Null дата релиза вызывает ошибку")
    void validateFilmWithNullReleaseDateShouldThrowException() {
        film.setReleaseDate(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Дата релиза не может быть пустой", exception.getMessage());
    }

    @Test
    @DisplayName("Дата релиза в будущем проходит валидацию (без ограничения)")
    void validateFilmWithFutureReleaseDateShouldPass() {
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Положительная продолжительность проходит валидацию")
    void validateFilmWithPositiveDurationShouldPass() {
        film.setDuration(1);
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    @DisplayName("Нулевая продолжительность вызывает ошибку")
    void validateFilmWithZeroDurationShouldThrowException() {
        film.setDuration(0);
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    @DisplayName("Отрицательная продолжительность вызывает ошибку")
    void validateFilmWithNegativeDurationShouldThrowException() {
        film.setDuration(-10);
        ValidationException exception = assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }
}