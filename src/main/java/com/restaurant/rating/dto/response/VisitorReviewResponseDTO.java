package com.restaurant.rating.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "DTO ответа с информацией об отзыве посетителя")
public record VisitorReviewResponseDTO(
        @Schema(description = "ID посетителя")
        @NonNull
        Long visitorId,

        @Schema(description = "ID ресторана", example = "1")
        @NonNull
        Long restaurantId,

        @Schema(description = "Рейтинг от 1 до 5", example = "4")
        @NonNull
        Integer rating,

        @Schema(description = "Текст отзыва", example = "Крайне вкусные крылышки")
        String review
) {}
