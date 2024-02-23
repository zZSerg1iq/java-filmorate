package ru.yandex.practicum.filmorate.db.dao.repository;

import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.Genre;
import ru.yandex.practicum.filmorate.db.dao.entity.MpaRate;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> getFilmById(long filmId);

    List<Film> findFilmByData(Film film);

    List<Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    int addUserLike(long filmId, long userId);

    int deleteUserLike(long filmId, long userId);

    List<Film> getTopRate(int count);

    List<Genre> getGenres();

    Genre getGenreById(long id);

    List<MpaRate> getMpaRateList();

    MpaRate getMpaRateById(long id);
}
