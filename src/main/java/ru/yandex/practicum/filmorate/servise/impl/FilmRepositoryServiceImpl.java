package ru.yandex.practicum.filmorate.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.servise.FilmRepositoryService;
import ru.yandex.practicum.filmorate.servise.UserRepositoryService;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmRepositoryServiceImpl implements FilmRepositoryService {

    private final FilmRepository filmRepository;
    private final UserRepositoryService userStorageService;

    @Autowired
    public FilmRepositoryServiceImpl(FilmRepository filmRepository, UserRepositoryService userStorageService) {
        this.filmRepository = filmRepository;
        this.userStorageService = userStorageService;
    }

    @Override
    public FilmDto getFilm(long filmId) {
        Optional<Film> filmOpt = filmRepository.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Фильм с id " + filmId + " не найден");
        }

        return new FilmMapper().entityToDto(filmOpt.get());
    }

    @Override
    public List<FilmDto> getFilmList() {
        return new FilmMapper().entityListToDtoList( filmRepository.getFilmList() );
    }

    @Override
    public FilmDto addFilm(FilmDto filmDto) {
        FilmMapper filmMapper = new FilmMapper();

        Film film = new FilmMapper().dtoToEntity(filmDto);
        Optional<Film> filmOpt = filmRepository.findFilmByData(film);
        if (filmOpt.isPresent()) {
            throw new DataConflictException("Добавляемый фильм уже находится в базе");
        }

        Film temp = filmRepository.addFilm(filmMapper.dtoToEntity(filmDto));
        log.info("Новый фильм добавлен: " + film);
        return filmMapper.entityToDto(temp);
    }

    @Override
    public FilmDto updateFilm(FilmDto filmDto) {
        Optional<Film> filmOpt = filmRepository.getFilm(filmDto.getId());
        if (filmOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + filmDto.getId() + " не существует.");
        }

        log.info("Данные фильма изменены: " + filmDto);
        filmRepository.updateFilm(new FilmMapper().dtoToEntity(filmDto));
        return filmDto;
    }

    @Override
    public void deleteFilm(long id) {
        Optional<Film> filmOpt = filmRepository.getFilm(id);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления фильма: фильм " + id + " не найден");
        }

        log.info("Новый фильм удален: " + filmOpt.get());
        filmRepository.deleteFilm(id);
    }

    @Override
    public FilmDto addUserLike(long filmId, long userId) {
        userStorageService.getUser(userId);
        Optional<Film> filmOpt = filmRepository.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка добавления лайка фильму: фильм " + filmId + " не найден");
        }

/*        Film film = filmOpt.get();
        if (!fil.addUserLike(userId)) {
            throw new DataConflictException("Ошибка добавления лайка фильму c id " + filmId + ". Лайк от этого пользователя уже стоит.");
        }

        log.info("Добавлен лайк к фильму: " + film.getName());
        filmRepository.updateFilm(film);
        return film;*/
        return null;
    }

    @Override
    public FilmDto deleteUserLike(long filmId, long userId) {
       /* userStorageService.getUser(userId);
        Optional<FilmDto> filmOpt = filmRepository.getFilm(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления лайка у фильма " + filmId + ". Фильм не найден");
        }

        FilmDto film = filmOpt.get();
        if (!film.deleteUserLike(userId)) {
            throw new DataNotFoundException("Ошибка удаления лайка у фильма c id " + filmId + ". Лайка не было.");
        }

        log.info("Удален лайк у фильма: " + film.getName());
        filmRepository.updateFilm(film);
        return film;*/
        return null;
    }

    @Override
    public List<FilmDto> getTopRate(int count) {
        return null;
/*        final Comparator<FilmDto> comparator = Comparator.comparingInt(o -> o.getUserLikes().size());

        return filmRepository.getFilmList().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }*/
    }
}
