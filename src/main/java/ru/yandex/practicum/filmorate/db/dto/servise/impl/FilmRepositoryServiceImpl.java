package ru.yandex.practicum.filmorate.db.dto.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.entity.MpaRateDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;
import ru.yandex.practicum.filmorate.db.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmRepositoryServiceImpl implements FilmRepositoryService {

    private final FilmRepository filmRepository;
    private final UserRepositoryService userRepositoryService;

    @Autowired
    public FilmRepositoryServiceImpl(FilmRepository filmRepository, UserRepositoryService userRepositoryService) {
        this.filmRepository = filmRepository;
        this.userRepositoryService = userRepositoryService;
    }

    @Override
    public FilmDto getFilm(long filmId) {
        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Фильм с id " + filmId + " не найден");
        }

        return new FilmMapper().filmEntityToDto(filmOpt.get());
    }

    @Override
    public List<FilmDto> getFilmList() {
        return new FilmMapper().filmEntityListToDtoList(filmRepository.getFilmList());
    }

    @Override
    public FilmDto addFilm(FilmDto filmDto) {
        FilmMapper filmMapper = new FilmMapper();
        System.out.println(filmDto);


        Film film = new FilmMapper().filmDtoToEntity(filmDto);
        List<Film> result = filmRepository.findFilmByData(film);
        if (result.size() > 0) {
            throw new DataConflictException("Добавляемый фильм уже находится в базе");
        }

        Film temp = filmRepository.addFilm(filmMapper.filmDtoToEntity(filmDto));
        log.info("Новый фильм добавлен: " + film);
        return filmMapper.filmEntityToDto(temp);
    }

    @Override
    public FilmDto updateFilm(FilmDto filmDto) {
        Optional<Film> filmOpt = filmRepository.getFilmById(filmDto.getId());
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + filmDto.getId() + " не существует.");
        }

        var temp = filmDto.getGenres();
        filmDto.setGenres(new ArrayList<>());
        for (GenreDto g : temp) {
            if (!filmDto.getGenres().contains(g)) {
                filmDto.getGenres().add(g);
            }
        }

        log.info("Данные фильма изменены: " + filmDto);
        filmRepository.updateFilm(new FilmMapper().filmDtoToEntity(filmDto));

        var result = filmRepository.getFilmById(filmDto.getId());
        if (result.isPresent()) {
            return new FilmMapper().filmEntityToDto(result.get());
        }

        throw new DataNotFoundException("Внутренняя ошибка: Ошибка обновления данных. Фильма с id " + filmDto.getId() + " не существует.");
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
        UserDto userDto = userRepositoryService.getUserById(userId);

        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);
        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка добавления лайка фильму: фильм " + filmId + " не найден");
        }

        FilmDto filmDto = new FilmMapper().filmEntityToDto(filmOpt.get());

        if (!filmDto.getUserLikes().contains(userDto)) {
            if (filmRepository.addUserLike(filmId, userId) > 0) {
                log.info("Добавлен лайк к фильму: " + filmOpt.get().getName());
                filmDto.getUserLikes().add(userDto);
            } else {
                log.error("Ошибка добавления лайка: " + filmOpt.get().getName());
                throw new InternalDataException("Ошибка добавления лайка");
            }
        } else {
            throw new DataConflictException("Ошибка добавления лайка фильму: этот пользователь уже ставил лайк ");
        }

        return filmDto;
    }

    @Override
    public FilmDto deleteUserLike(long filmId, long userId) {
        UserDto userDto = userRepositoryService.getUserById(userId);
        Optional<Film> filmOpt = filmRepository.getFilmById(filmId);

        if (filmOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления лайка у фильма: фильм " + filmId + " не найден");
        }

        FilmDto filmDto = new FilmMapper().filmEntityToDto(filmOpt.get());

        // Оставил эту загадку пока так, потому что из за внешнего IF
        // по совершенно непонятным и необъяснимым для меня причинам на гите тест
        // валится, выбрасывая исключение, хотя на локале все проверки, все тесты,
        // все сравнения работают и проходят. Не понимаю, почему...
/*        System.out.println("user1 contains in film list: "+filmDto.getUserLikes().contains(userDto));
        System.out.println("users: ----------------------");
        System.out.println("users1: "+userDto);
        System.out.println("user in list: "+filmDto.getUserLikes().get(0));
        System.out.println("user1 equals userInList: "+filmDto.getUserLikes().get(0).equals(userDto));

        if (filmDto.getUserLikes().contains(userDto)) {*/
        if (filmRepository.deleteUserLike(filmId, userId) > 0) {
            log.info("удален лайк у фильма: " + filmOpt.get().getName());
            filmDto.getUserLikes().remove(userDto);
        } else {
            log.error("Ошибка удаления лайка: " + filmOpt.get().getName());
            throw new InternalDataException("Ошибка удаления лайка");
        }
         /* } else {
              throw new DataConflictException("Ошибка удаления лайка у фильма: этот пользователь еще не ставил лайк ");
         }
*/
        return filmDto;
    }

    @Override
    public List<FilmDto> getTopRate(int count) {
        var topRate = filmRepository.getTopRate(count);
        return new FilmMapper().filmEntityListToDtoList(topRate);
    }

    @Override
    public List<GenreDto> getGenresList() {
        return new FilmMapper().genresEntityListToDtoList(filmRepository.getGenres());
    }

    @Override
    public GenreDto getGenreById(long id) {
        var genre = filmRepository.getGenreById(id);
        if (genre == null) {
            throw new DataNotFoundException("Жанр не существует");
        }
        return new FilmMapper().genreEntityToDto(genre);
    }

    @Override
    public List<MpaRateDto> getMpaList() {
        return new FilmMapper().mpaRateEntityToDto(filmRepository.getMpaRateList());
    }

    @Override
    public MpaRateDto getMpaById(long id) {
        var mpa = filmRepository.getMpaRateById(id);
        if (mpa == null) {
            throw new DataNotFoundException("Рейтинг не найден");
        }
        return new FilmMapper().mpaRateEntityToDto(mpa);
    }
}
