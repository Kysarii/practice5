package com.restaurant.rating.dto.response;

import com.restaurant.rating.enums.KitchenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.math.BigDecimal;

@Schema(description = "DTO ответа с информацией о ресторане")
public record RestaurantResponseDTO (
        @Schema(description = "Уникальный идентификатор ресторана")
        Long id,

        @Schema(description = "Название ресторана", example = "The Happy Bull")
        @NonNull
        String name,

        @Schema(description = "Описание ресторана", example = "Бургеры и прочее")
        String description,

        @Schema(description = "Тип кухни", example = "AMERICAN")
        @NonNull
        KitchenType kitchenType,

        @Schema(description = "Средний чек", example = "1200.0")
        @NonNull
        Double averageCheck,

        @Schema(description = "Рейтинг пользователей", example = "4.2")
        @NonNull
        BigDecimal userRating
) {}


