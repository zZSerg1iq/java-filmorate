package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.db.dto.entity.MpaRateDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;

import java.util.List;

@RestController
public class MPAController {

    private final FilmRepositoryService filmStorageService;

    @Autowired
    public MPAController(FilmRepositoryService filmStorageService) {
        this.filmStorageService = filmStorageService;
    }

    @GetMapping("/mpa")
    public ResponseEntity<List<MpaRateDto>> getMpaList() {
        return ResponseEntity.ok(filmStorageService.getMpaList());
    }

    @GetMapping("/mpa/{id}")
    public ResponseEntity<MpaRateDto> getMpaById(@PathVariable("id") int id) {
        return ResponseEntity.ok(filmStorageService.getMpaById(id));
    }
}
