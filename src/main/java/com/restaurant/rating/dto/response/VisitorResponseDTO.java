package com.restaurant.rating.dto.response;

import com.restaurant.rating.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "DTO ответа с информацией о посетителе")
public record VisitorResponseDTO(
        @Schema(description = "Уникальный идентификатор посетителя")
        Long id,

        @Schema(description = "Имя посетителя", example = "Иван Иванов")
        String name,

        @Schema(description = "Возраст посетителя", example = "19")
        @NonNull
        Integer age,

        @Schema(description = "Пол посетителя", example = "MALE")
        @NonNull
        Gender gender
) {}
