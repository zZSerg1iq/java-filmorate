package ru.yandex.practicum.filmorate.db.dto.servise;

import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;

import java.util.List;

public interface FilmRepositoryService {

    FilmDto getFilm(long filmId);

    List<FilmDto> getFilmList();

    FilmDto addFilm(FilmDto film);

    FilmDto updateFilm(FilmDto film);

    void deleteFilm(long id);

    FilmDto addUserLike(long filmId, long userId);

    FilmDto deleteUserLike(long filmId, long userId);

    List<FilmDto> getTopRate(int count);
}
