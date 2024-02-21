package ru.yandex.practicum.filmorate.db.dto.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dao.entity.MpaDto;
import ru.yandex.practicum.filmorate.db.dao.entity.UserLikes;
import ru.yandex.practicum.filmorate.db.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;
import ru.yandex.practicum.filmorate.db.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.db.mapping.UserMapper;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;

import java.util.List;
import java.util.Optional;

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
        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Фильм с id " + filmId + " не найден");
        }

        return new FilmMapper().entityToDto(filmOpt.get());
    }

    @Override
    public List<FilmDto> getFilmList() {
        return new FilmMapper().entityListToDtoList(filmRepository.getFilmList());
    }

    @Override
    public FilmDto addFilm(FilmDto filmDto) {
        FilmMapper filmMapper = new FilmMapper();

        Film film = new FilmMapper().dtoToEntity(filmDto);
        List<Film> result = filmRepository.findFilmByData(film);
        if (result.size() > 0) {
            throw new DataConflictException("Добавляемый фильм уже находится в базе");
        }

        Film temp = filmRepository.addFilm(filmMapper.dtoToEntity(filmDto));
        log.info("Новый фильм добавлен: " + film);
        return filmMapper.entityToDto(temp);
    }

    @Override
    public FilmDto updateFilm(FilmDto filmDto) {
        Optional<Film> filmOpt = filmRepository.getFilmById(filmDto.getId());
        if (filmOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + filmDto.getId() + " не существует.");
        }

        log.info("Данные фильма изменены: " + filmDto);
        filmRepository.updateFilm(new FilmMapper().dtoToEntity(filmDto));
        return filmDto;
    }

    @Override
    public void deleteFilm(long id) {
        Optional<Film> filmOpt = filmRepository.getFilmById(id);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления фильма: фильм " + id + " не найден");
        }

        log.info("Новый фильм удален: " + filmOpt.get());
        filmRepository.deleteFilm(id);
    }

    @Override
    public FilmDto addUserLike(long filmId, long userId) {
        UserDto userDto = userStorageService.getUser(userId);

        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка добавления лайка фильму: фильм " + filmId + " не найден");
        }

        FilmDto filmDto = new FilmMapper().entityToDto(filmOpt.get());

        if (!filmOpt.get().getUserLikes().contains(new UserMapper().dtoToEntity(userDto))) {
            filmRepository.addUserLike(filmId, userId);
            log.info("Добавлен лайк к фильму: " + filmOpt.get().getName());
            filmDto.getUserLikes().add(userDto);
        } else {
            throw new DataConflictException("Ошибка добавления лайка фильму: этот пользователь уже ставил лайк ");
        }


        return filmDto;
    }

    @Override
    public FilmDto deleteUserLike(long filmId, long userId) {
        UserDto userDto = userStorageService.getUser(userId);

        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления лайка у фильма: фильм " + filmId + " не найден");
        }

        FilmDto filmDto = new FilmMapper().entityToDto(filmOpt.get());

        if (filmOpt.get().getUserLikes().contains(new UserMapper().dtoToEntity(userDto))) {
            filmRepository.deleteUserLike(filmId, userId);
            log.info("удален лайк у фильма: " + filmOpt.get().getName());
            filmDto.getUserLikes().remove(userDto);
        } else {
            throw new DataConflictException("Ошибка удаления лайка у фильма: этот пользователь еще не ставил лайк ");
        }

        return filmDto;
    }

    @Override
    public List<FilmDto> getTopRate(int count) {
        var topRate = filmRepository.getTopRate(count);
        return new FilmMapper().entityListToDtoList(topRate);
    }

    @Override
    public List<GenreDto> getGenresList() {
        return null;
    }

    @Override
    public GenreDto getGenreById() {
        return null;
    }

    @Override
    public List<MpaDto> getMpaList() {
        return null;
    }

    @Override
    public MpaDto getMpaById() {
        return null;
    }
}
