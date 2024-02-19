package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
        film.setGenre(filmDto.getGenre());
        film.setTmaRate(filmDto.getTmaRate());

        return film;
    }

    public FilmDto entityToDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDuration(film.getDuration());
        filmDto.setDescription(film.getDescription());
        filmDto.setGenre(film.getGenre());
        filmDto.setTmaRate(film.getTmaRate());
        if (film.getUserLikes().size() > 0) {
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
