package com.restaurant.rating.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO для создания/обновления отзыва посетителя")
public record VisitorReviewRequestDTO(
        Long visitorId,

        @Schema(description = "ID ресторана", example = "1")
        @NotNull(message = "ID ресторана обязателен")
        @Positive(message = "ID ресторана должен быть положительным числом")
        Long restaurantId,

        @Schema(description = "Рейтинг от 1 до 5", example = "4")
        @NotNull(message = "Рейтинг обязателен")
        @Min(value = 1, message = "Рейтинг должен быть не менее 1")
        @Max(value = 5, message = "Рейтинг должен быть не более 5")
        Integer rating,

        @Schema(description = "Текст отзыва", example = "Крайне вкусные крылышки")
        String review
) {}
