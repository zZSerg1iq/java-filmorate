package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmStorageService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorageService filmStorageService;

    @Autowired
    public FilmController(FilmStorageService filmStorageService) {
        this.filmStorageService = filmStorageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable long id) {
        return ResponseEntity.ok(filmStorageService.getFilm(id));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilmList() {
        return ResponseEntity.ok(filmStorageService.getFilmList());
    }

    @PostMapping()
    public ResponseEntity<Film> addFilm(@NotNull @Valid @RequestBody Film film) {
        return ResponseEntity.ok(filmStorageService.addFilm(film));
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(filmStorageService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmStorageService.deleteFilm(id);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopRateList(
            @RequestParam(value = "count", required = false) String count) {
        final int defaultValue = 10;

        if (count != null && !count.isBlank()) {
            int filmCount = Integer.parseInt(count);
            return ResponseEntity.ok(filmStorageService.getTopRate(filmCount));
        } else {
            return ResponseEntity.ok(filmStorageService.getTopRate(defaultValue));
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addUserLike(
            @PathVariable("id") long filmId,
            @PathVariable long userId) {

        return ResponseEntity.ok(filmStorageService.addUserLike(filmId, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteUserLike(
            @PathVariable("id") long filmId,
            @PathVariable long userId) {

        return ResponseEntity.ok(filmStorageService.deleteUserLike(filmId, userId));
    }
}
