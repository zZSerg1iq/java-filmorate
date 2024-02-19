package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmDataStorage;
    private long id = 1;


    public InMemoryFilmStorage() {
        filmDataStorage = new HashMap<>();
    }

    @Override
    public Optional<Film> getFilm(long filmId) {
        return Optional.ofNullable(filmDataStorage.get(filmId));
    }

    @Override
    public Optional<Film> findFilmByData(Film film) {
        Film result = null;

        for (Film f : filmDataStorage.values()) {
            if (f.equals(film)) {
                result = f;
                break;
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(filmDataStorage.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id++);
        filmDataStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return filmDataStorage.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(long id) {
        filmDataStorage.remove(id);
    }

}
