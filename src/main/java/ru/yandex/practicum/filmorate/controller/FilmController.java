package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmDataStorageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmDataStorageService filmStorageService;

    @Autowired
    public FilmController(FilmDataStorageService filmStorageService) {
        this.filmStorageService = filmStorageService;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable long id) {
        var film = filmStorageService.getFilm(id);
        if (film.getId() != -1) {
            return ResponseEntity.ok(film);
        }
        return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilmList() {
        var filmList = filmStorageService.getFilmList();
        return ResponseEntity.ok(filmList);
    }

    @PostMapping()
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        var addedFilm = filmStorageService.addFilm(film);

        if (addedFilm.getId() != -1) {
            log.info("Film added: " + addedFilm);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        }
        log.info("Add film conflict: " + film);
        return new ResponseEntity<>(film, HttpStatus.CONFLICT);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        var updateResult = filmStorageService.updateFilm(film);

        if (updateResult.getId() != -1) {
            log.info("Film updated: " + updateResult);
            return ResponseEntity.ok(updateResult);
        }
        log.info("Error update film: " + film);
        return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmStorageService.deleteFilm(id);
    }

}
