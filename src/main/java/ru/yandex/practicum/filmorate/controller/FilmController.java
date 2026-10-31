package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        List<Film> filmList = new ArrayList<>(films.values());
        log.info("Возвращено {} фильмов", filmList.size());
        return filmList;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);

        try {
            FilmValidator.validate(film);
            film.setId(currentId++);
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен с id: {}, название: {}", film.getId(), film.getName());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);

        try {
            if (film.getId() == 0) {
                log.warn("Попытка обновить фильм без указания ID");
                throw new ValidationException("Id должен быть указан");
            }

            if (!films.containsKey(film.getId())) {
                log.warn("Фильм с id {} не найден для обновления", film.getId());
                throw new ValidationException("Фильм с id " + film.getId() + " не найден");
            }

            FilmValidator.validate(film);
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлен с id: {}, название: {}", film.getId(), film.getName());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма с id {}: {}",
                    film.getId(), e.getMessage());
            throw e;
        }

    }
}
