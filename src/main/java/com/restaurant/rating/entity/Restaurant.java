package com.restaurant.rating.entity;

import com.restaurant.rating.enums.KitchenType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private KitchenType kitchenType;

    @Column(nullable = false)
    private Double averageCheck;

    @Column(nullable = false)
    private BigDecimal userRating;

}
