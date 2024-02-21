package ru.yandex.practicum.filmorate.db.dto.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.db.enums.MotionPictureAssociationRate;
import ru.yandex.practicum.filmorate.validation.DateTimeMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class FilmDto {

    private long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeMin(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private String genre;

    private MotionPictureAssociationRate tmaRate;

    private List<UserDto> userLikes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDto film = (FilmDto) o;
        return duration == film.duration && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration);
    }
}
