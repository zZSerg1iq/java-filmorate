package ru.yandex.practicum.filmorate.db.dto.servise;

import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.entity.MpaRateDto;

import java.util.List;
import java.util.Set;

public interface FilmRepositoryService {

    FilmDto getFilm(long filmId);

    List<FilmDto> getFilmList();

    FilmDto addFilm(FilmDto film);

    FilmDto updateFilm(FilmDto film);

    void deleteFilm(long id);

    FilmDto addUserLike(long filmId, long userId);

    FilmDto deleteUserLike(long filmId, long userId);

    List<FilmDto> getTopRate(int count);

    List<GenreDto> getGenresList();

    GenreDto getGenreById(long id);

    List<MpaRateDto> getMpaList();

    MpaRateDto getMpaById(long id);
}
