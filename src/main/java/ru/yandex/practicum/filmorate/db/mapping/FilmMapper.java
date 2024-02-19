package ru.yandex.practicum.filmorate.db.mapping;

import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.User;

import java.util.ArrayList;
import java.util.List;

public class FilmMapper {

    public List<FilmDto> entityListToDtoList(List<Film> filmList) {
        List<FilmDto> filmDtoList = new ArrayList<>();

        if (filmList != null && filmList.size() > 0) {
            for (Film film : filmList) {
                filmDtoList.add(entityToDto(film));
            }
        }
        return filmDtoList;
    }

    public Film dtoToEntity(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDuration(filmDto.getDuration());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setGenre(filmDto.getGenre());
        film.setMpaRate(filmDto.getTmaRate());

        return film;
    }

    public FilmDto entityToDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDuration(film.getDuration());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setGenre(film.getGenre());
        filmDto.setTmaRate(film.getMpaRate());
        if (film.getUserLikes() != null && film.getUserLikes().size() > 0) {
            filmDto.setUserLikes(getUserLikesList(film.getUserLikes()));
        }
        return filmDto;
    }

    private List<UserDto> getUserLikesList(List<User> userLikes) {
        UserMapper userMapper = new UserMapper();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userLikes) {
            UserDto userDto = userMapper.entityToSimpleDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }


}
