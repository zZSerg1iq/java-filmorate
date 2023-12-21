package ru.yandex.practicum.filmorate.servise.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmDataStorageService;
import ru.yandex.practicum.filmorate.storage.FilmDataStorage;
import java.util.List;

@Service
public class FilmDataStorageServiceImpl implements FilmDataStorageService {

    private final FilmDataStorage filmDataStorage;

    @Autowired
    public FilmDataStorageServiceImpl(FilmDataStorage filmDataStorage) {
        this.filmDataStorage = filmDataStorage;
    }

    @Override
    public Film getFilm(long filmId) {
        return filmDataStorage.getFilm(filmId).orElseGet(Film::new);
    }

    @Override
    public List<Film> getFilmList() {
        return filmDataStorage.getFilmList();
    }

    @Override
    public Film addFilm(Film film) {
        return filmDataStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmDataStorage.updateFilm(film);
    }

    @Override
    public void deleteFilm(long id) {
        filmDataStorage.deleteFilm(id);
    }
}
