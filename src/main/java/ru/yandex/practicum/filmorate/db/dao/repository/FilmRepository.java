package ru.yandex.practicum.filmorate.db.dao.repository;

import ru.yandex.practicum.filmorate.db.dao.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> getFilmById(long filmId);

    List<Film> findFilmByData(Film film);

    List<Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    boolean addUserLike(long filmId, long userId);

    boolean deleteUserLike(long filmId, long userId);

    List<Film> getTopRate(int count);

}
