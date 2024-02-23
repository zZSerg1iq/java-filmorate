package ru.yandex.practicum.filmorate.db.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpaRateDto {

    @Positive
    private long id;
    private String rate;
//    private String description;

}
