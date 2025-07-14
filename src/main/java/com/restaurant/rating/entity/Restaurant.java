package com.restaurant.rating.entity;

import com.restaurant.rating.enums.KitchenType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.Builder;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
public class Restaurant {
    private Long id;
    @NonNull
    private String restaurantName;
    private String description;
    @NonNull
    private KitchenType kitchenType;
    @NonNull
    private Double averageCheck;
    @NonNull
    private BigDecimal userRating;

}
