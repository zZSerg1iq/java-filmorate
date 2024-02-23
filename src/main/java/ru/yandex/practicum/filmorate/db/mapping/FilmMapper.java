package ru.yandex.practicum.filmorate.db.mapping;

import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.Genre;
import ru.yandex.practicum.filmorate.db.dao.entity.MpaRate;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.entity.MpaRateDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;

import java.util.ArrayList;
import java.util.List;

public class FilmMapper {

    public List<FilmDto> filmEntityListToDtoList(List<Film> filmList) {
        List<FilmDto> filmDtoList = new ArrayList<>();

        if (filmList != null && filmList.size() > 0) {
            for (Film film : filmList) {
                filmDtoList.add(filmEntityToDto(film));
            }
        }
        return filmDtoList;
    }

    public Film filmDtoToEntity(FilmDto filmDto) {
        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .duration(filmDto.getDuration())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .mpa(mpaRateDtoToEntity(filmDto.getMpa()))
                .genres(genresDtoListToEntityList(filmDto.getGenres()))
                .build();
    }


    public FilmDto filmEntityToDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .duration(film.getDuration())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .genres(genresEntityListToDtoList(film.getGenres()))
                .mpa(mpaRateEntityToDto(film.getMpa()))
                .userLikes(userLikesEntityListToDtoList(film.getUserLikes()))
                .build();
    }


    public MpaRate mpaRateDtoToEntity(MpaRateDto mpaRateDto) {
        return MpaRate.builder()
                .id(mpaRateDto.getId())
                .rate(mpaRateDto.getName())
                .description(mpaRateDto.getDescription())
                .build();
    }

    public List<MpaRateDto> mpaRateEntityToDto(List<MpaRate> mpaRate) {
        List<MpaRateDto> mpaRateDtos = new ArrayList<>();

        for (MpaRate m : mpaRate) {
            mpaRateDtos.add(mpaRateEntityToDto(m));
        }
        return mpaRateDtos;
    }

    public MpaRateDto mpaRateEntityToDto(MpaRate mpaRate) {
        return MpaRateDto.builder()
                .id(mpaRate.getId())
                .name(mpaRate.getRate())
                .description(mpaRate.getDescription())
                .build();
    }

    private List<Genre> genresDtoListToEntityList(List<GenreDto> genres) {
        List<Genre> genreList = new ArrayList<>();

        if (genres == null || genres.size() == 0) {
            return genreList;
        }

        for (GenreDto gen : genres) {
            genreList.add(
                    Genre.builder()
                            .id(gen.getId())
                            .name(gen.getName())
                            .build()
            );
        }
        return genreList;
    }

    public List<GenreDto> genresEntityListToDtoList(List<Genre> genres) {
        List<GenreDto> genreDtos = new ArrayList<>();

        if (genres == null || genres.size() == 0) {
            return genreDtos;
        }

        for (Genre genre : genres) {
            genreDtos.add(genreEntityToDto(genre));
        }
        return genreDtos;
    }

    public GenreDto genreEntityToDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    private List<UserDto> userLikesEntityListToDtoList(List<User> userLikes) {
        List<UserDto> userDtoList = new ArrayList<>();

        if (userLikes == null || userLikes.size() == 0) {
            return userDtoList;
        }

        UserMapper userMapper = new UserMapper();
        for (User user : userLikes) {
            UserDto userDto = userMapper.entityToSimpleDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }


}
