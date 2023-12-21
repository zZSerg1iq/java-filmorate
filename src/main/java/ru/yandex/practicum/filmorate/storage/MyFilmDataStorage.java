package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Component
public class MyFilmDataStorage implements FilmDataStorage {

    private final Map<Long, Film> filmDataStorage;
    private final Set<Film> films;
    private long id = 1;


    public MyFilmDataStorage() {
        filmDataStorage = new HashMap<>();
        films = new HashSet<>();
    }

    @Override
    public Optional<Film> getFilm(long filmId) {
        return Optional.ofNullable(filmDataStorage.get(filmId));
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(filmDataStorage.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (films.contains(film)) {
            film.setId(-1);
            return film;
        }

        film.setId(id++);
        films.add(film);
        filmDataStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmDataStorage.containsKey(film.getId())) {
            filmDataStorage.put(film.getId(), film);
            return film;
        }
        film.setId(-1);
        return film;
    }

    @Override
    public void deleteFilm(long id) {
        Film film = filmDataStorage.get(id);
        if (film != null) {
            filmDataStorage.remove(id);
            films.remove(film);
        }
    }
}
