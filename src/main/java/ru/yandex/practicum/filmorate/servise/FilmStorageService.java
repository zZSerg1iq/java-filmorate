package ru.yandex.practicum.filmorate.servise;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorageService {

    Film getFilm(long filmId);

    List<Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    Film addUserLike(long filmId, long userId);

    Film deleteUserLike(long filmId, long userId);

    List<Film> getTopRate(int count);
}
