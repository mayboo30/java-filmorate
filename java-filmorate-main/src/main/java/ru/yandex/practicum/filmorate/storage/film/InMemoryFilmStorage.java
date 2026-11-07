package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @Override
    public Film add(Film film) {
        log.info("Добавление фильма: {}", film);
        FilmValidator.validate(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен с id: {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("Обновление фильма: {}", film);
        if (film.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("Фильм обновлен с id: {}", film.getId());
        return film;
    }

    @Override
    public Film delete(int id) {
        log.info("Удаление фильма с id: {}", id);
        Film removed = films.remove(id);
        if (removed == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return removed;
    }

    @Override
    public Film getById(int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean existsById(int id) {
        return films.containsKey(id);
    }
}