package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmRepositoryService filmStorageService;

    @Autowired
    public FilmController(FilmRepositoryService filmStorageService) {
        this.filmStorageService = filmStorageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable long id) {
        return ResponseEntity.ok(filmStorageService.getFilmById(id));
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> getFilmList() {
        return ResponseEntity.ok(filmStorageService.getFilmList());
    }

    @PostMapping()
    public ResponseEntity<FilmDto> addFilm(@NotNull @Valid @RequestBody FilmDto film) {
        return ResponseEntity.ok(filmStorageService.addFilm(film));
    }

    @PutMapping()
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody FilmDto film) {
        return ResponseEntity.ok(filmStorageService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmStorageService.deleteFilm(id);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getTopRateList(@RequestParam(value = "count", defaultValue = "10") String count) {
        int filmCount = Integer.parseInt(count);
        return ResponseEntity.ok(filmStorageService.getTopRate(filmCount));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> addUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {

        return ResponseEntity.ok(filmStorageService.addUserLike(filmId, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> deleteUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {

        return ResponseEntity.ok(filmStorageService.deleteUserLike(filmId, userId));
    }

}
