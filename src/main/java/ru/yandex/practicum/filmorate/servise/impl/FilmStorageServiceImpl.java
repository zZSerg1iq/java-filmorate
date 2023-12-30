package ru.yandex.practicum.filmorate.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmStorageService;
import ru.yandex.practicum.filmorate.servise.UserStorageService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmStorageServiceImpl implements FilmStorageService {

    private final FilmStorage filmStorage;
    private final UserStorageService userStorageService;

    @Autowired
    public FilmStorageServiceImpl(FilmStorage filmStorage, UserStorageService userStorageService) {
        this.filmStorage = filmStorage;
        this.userStorageService = userStorageService;
    }

    @Override
    public Film getFilm(long filmId) {
        Optional<Film> filmOpt = filmStorage.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            log.info("Фильм с id " + filmId + " не найден");
            throw new DataNotFoundException("Фильм с id " + filmId + " не найден");

        }

        return filmOpt.get();
    }

    @Override
    public List<Film> getFilmList() {
        return filmStorage.getFilmList();
    }

    @Override
    public Film addFilm(Film film) {
        Optional<Film> filmOpt = filmStorage.findFilmByData(film);
        if (filmOpt.isPresent()) {
            log.info("Добавляемый фильм уже находится в базе");
            throw new DataConflictException("Добавляемый фильм уже находится в базе");
        }

        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        Optional<Film> filmOpt = filmStorage.getFilm(film.getId());
        if (filmOpt.isEmpty()) {
            log.warn("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + film.getId() + " не существует.");
            throw new InternalDataException("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + film.getId() + " не существует.");
        }

        filmStorage.updateFilm(film);
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        Optional<Film> filmOpt = filmStorage.getFilm(id);
        if (filmOpt.isEmpty()) {
            log.info("Ошибка удаления фильма: фильм " + id + " не найден");
            throw new DataNotFoundException("Ошибка удаления фильма: фильм " + id + " не найден");
        }

        filmStorage.deleteFilm(id);
    }

    @Override
    public Film addUserLike(long filmId, long userId) {
        userStorageService.getUser(userId);
        Optional<Film> filmOpt = filmStorage.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            log.info("Ошибка добавления лайка фильму " + filmId + " не найден");
            throw new DataNotFoundException("Ошибка добавления лайка фильму: фильм " + filmId + " не найден");
        }

        Film film = filmOpt.get();
        if (!film.addUserLike(userId)) {
            log.info("Ошибка добавления лайка фильму c id " + filmId + ". Лайк от этого пользователя уже стоит.");
            throw new DataConflictException("Ошибка добавления лайка фильму c id " + filmId + ". Лайк от этого пользователя уже стоит.");
        }

        filmStorage.updateFilm(film);
        return film;
    }

    @Override
    public Film deleteUserLike(long filmId, long userId) {
        userStorageService.getUser(userId);
        Optional<Film> filmOpt = filmStorage.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            log.info("Ошибка удаления лайка у фильма " + filmId + ". Фильм не найден");
            throw new DataNotFoundException("Ошибка удаления лайка у фильма " + filmId + ". Фильм не найден");
        }

        Film film = filmOpt.get();
        if (!film.deleteUserLike(userId)) {
            log.info("Ошибка удаления лайка у фильма c id " + filmId + ". Лайка не было.");
            throw new DataNotFoundException("Ошибка удаления лайка у фильма c id " + filmId + ". Лайка не было.");
        }

        filmStorage.updateFilm(film);
        return film;
    }

    @Override
    public List<Film> getTopRate(int count) {
        List<Film> tempList = filmStorage.getFilmList();
        if (tempList.size() == 0) {
            return tempList;
        }

        tempList.sort(Comparator.comparingInt(o -> o.getUserLikes().size()));
        return tempList
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
