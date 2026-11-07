package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private static final int POPULAR_FILMS_LIMIT = 10;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int filmId, int userId) {
        log.info("Пользватель {} ставит лайк фильму {}", userId, filmId);
        Film film = filmStorage.getById(filmId);
        userStorage.getById(userId);
        film.getLikes().add(userId);
        log.info("Фильм {} теперь имеет {} лайков", filmId, film.getLikes().size());
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        log.info("Пользователь {} убирает лайк с фильма {}", userId, filmId);
        Film film = filmStorage.getById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь " + userId + " не ставил лайк фильму " + filmId);
        }
        film.getLikes().remove(userId);
        log.info("Фильм {} теперь имеет {} лайков", filmId, film.getLikes().size());
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение {} популярных фильмов", count);
        int limit = POPULAR_FILMS_LIMIT;
        if (count > 0) {
            limit = count;
        }
        return filmStorage.getAll().stream().sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size())).limit(limit).collect(Collectors.toList());
    }
}