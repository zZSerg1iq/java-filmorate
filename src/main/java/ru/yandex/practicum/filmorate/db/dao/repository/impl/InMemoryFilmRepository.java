package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.repository.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;

//@Component
public class InMemoryFilmRepository implements FilmRepository {

    private final Map<Long, Film> filmDataStorage;

    private final Map<Long, Set<Long>> userLikesTemp;
    private final Map<Long, Set<Long>> userLikes;

    private long id = 1;


    public InMemoryFilmRepository() {
        filmDataStorage = new HashMap<>();
        userLikesTemp = new HashMap<>();

        userLikes = new TreeMap<>((o1, o2) -> Long.compare(userLikesTemp.get(o1).size(), userLikesTemp.get(o2).size()));
    }


    @Override
    public Optional<Film> getFilmById(long filmId) {
        return Optional.ofNullable(filmDataStorage.get(filmId));
    }

    @Override
    public List<Film> findFilmByData(Film film) {
        List<Film> films = new ArrayList<>();

        for (Film f : filmDataStorage.values()) {
            if (f.equals(film)) {
                films.add(f);
                break;
            }
        }

        return films;
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
        userLikesTemp.remove(id);
    }

    @Override
    public boolean addUserLike(long filmId, long userId) {
        if (userLikesTemp.get(userId) == null) {
            userLikesTemp.put(filmId, new HashSet<>());
        }
        return userLikesTemp.get(filmId).add(userId);
    }

    @Override
    public boolean deleteUserLike(long filmId, long userId) {
        if (userLikesTemp.get(userId) == null) {
            return false;
        }
        return userLikesTemp.get(filmId).remove(userId);
    }

    @Override
    public List<Film> getTopRate(int count) {
        userLikes.clear();
        userLikes.putAll(userLikesTemp);

        return userLikes
                .entrySet()
                .stream()
                .limit(count)
                .map(longSetEntry -> filmDataStorage.get(longSetEntry.getKey()))
                .collect(Collectors.toList());
    }
}
