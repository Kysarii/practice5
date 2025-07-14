package com.restaurant.rating.entity;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.Builder;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
public class VisitorReview {
    private Long id;
    @NonNull
    private Long restaurantId;
    @NonNull
    private Integer rating;
    private String review;
}
