package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MpaRate {

    private long id;
    private String rate;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MpaRate rate1 = (MpaRate) o;
        return Objects.equals(rate, rate1.rate) && Objects.equals(description, rate1.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, description);
    }
}
