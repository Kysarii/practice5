package com.restaurant.rating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VisitorReviewId implements Serializable {
    @Column(name = "visitor_id", nullable = false)
    private Long visitorId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
}
