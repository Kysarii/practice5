package com.restaurant.rating.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KitchenType {
    EUROPEAN("Европейская"),
    ITALIAN("Итальянская"),
    CHINESE("Китайская"),
    JAPANESE("Японская"),
    RUSSIAN("Русская"),
    MEXICAN("Мексиканская"),
    AMERICAN("Американская");


    private final String type;

}
