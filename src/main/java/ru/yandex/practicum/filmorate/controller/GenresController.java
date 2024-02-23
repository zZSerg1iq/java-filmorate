package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;

import java.util.List;

@RestController
public class GenresController {

    private final FilmRepositoryService filmStorageService;

    public GenresController(FilmRepositoryService filmStorageService) {
        this.filmStorageService = filmStorageService;
    }

    @GetMapping("/genres")
    public ResponseEntity<List<GenreDto>> getGenreList() {
        return ResponseEntity.ok(filmStorageService.getGenresList());
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable("id") int id) {
        return ResponseEntity.ok(filmStorageService.getGenreById(id));
    }
}
