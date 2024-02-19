package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> getFilm(long filmId);

    Optional<Film> findFilmByData(Film film);

    List<Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

}
