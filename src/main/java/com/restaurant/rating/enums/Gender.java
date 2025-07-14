package com.restaurant.rating.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Мужчина"),
    FEMALE("Женщина");

    private final String gender;
}
