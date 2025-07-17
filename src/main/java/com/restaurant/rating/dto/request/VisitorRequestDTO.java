package com.restaurant.rating.dto.request;

import com.restaurant.rating.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO для создания/обновления посетителя")
public record VisitorRequestDTO(
        @Schema(description = "Имя посетителя", example = "Иван Иванов")
        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 20, message = "Имя должно содержать от 2 до 20 символов")
        String name,

        @Schema(description = "Возраст посетителя", example = "19", minimum = "0")
        @NotNull(message = "Возраст обязателен")
        @Min(value = 0, message = "Возраст не может быть отрицательным")
        Integer age,

        @Schema(description = "Пол посетителя", example = "MALE")
        @NotNull(message = "Пол обязателен")
        Gender gender
) {}
