package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен запрос на получение фильма с id: {}", id);
        return filmStorage.getById(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        return filmStorage.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}