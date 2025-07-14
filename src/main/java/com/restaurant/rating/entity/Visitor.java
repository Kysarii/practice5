package com.restaurant.rating.entity;

import com.restaurant.rating.enums.Gender;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;


@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
public class Visitor {
    private Long id;
    private String name;
    @NonNull
    private Integer age;
    @NonNull
    private Gender gender;
}
