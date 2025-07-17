package com.restaurant.rating.dto.request;

import com.restaurant.rating.enums.KitchenType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "DTO для создания/обновления ресторана")
public record RestaurantRequestDTO(
        @Schema(description = "Название ресторана", example = "The Happy Bull")
        @NotBlank(message = "Название ресторана не может быть пустым")
        @Size(min = 2, max = 100, message = "Название ресторана должно содержать от 2 до 100 символов")
        String restaurantName,

        @Schema(description = "Описание ресторана", example = "Бургеры и прочее")
        String description,

        @Schema(description = "Тип кухни", example = "AMERICAN")
        @NotNull(message = "Тип кухни обязателен")
        KitchenType kitchenType,

        @Schema(description = "Средний чек в рублях", example = "1200.0")
        @NotNull(message = "Средний чек обязателен")
        @Positive(message = "Средний чек должен быть положительным числом")
        Double averageCheck,

        @Schema(description = "Рейтинг пользователей")
        @NotNull(message = "Рейтинг пользователей обязателен")
        @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
        @DecimalMax(value = "5.0", message = "Рейтинг не может превышать 5")
        BigDecimal userRating
) {}
