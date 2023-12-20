package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DateTimeMin;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private long id = -1;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeMin(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration);
    }
}
