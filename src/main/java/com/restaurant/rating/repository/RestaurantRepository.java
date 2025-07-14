package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();

    public Restaurant save(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Ресторан не может быть null");
        }

        if (restaurant.getId() != null) {
            Optional<Restaurant> optionalRestaurant = restaurants.stream()
                    .filter(rest -> Objects.equals(rest.getId(), restaurant.getId()))
                    .findFirst();

            if (optionalRestaurant.isPresent()) {
                optionalRestaurant.get().setUserRating(restaurant.getUserRating());
                return optionalRestaurant.get();
            } else {
                throw new IllegalStateException("Ресторан с таким ID не найден");
            }
        } else {
            if (restaurants.contains(restaurant)) {
                throw new IllegalStateException("Такой Ресторан уже существует");
            }
        }
        if (restaurants.isEmpty()) {
            restaurant.setId(0L);
        } else {
            restaurant.setId(restaurants.getLast().getId() + 1);
        }

        restaurants.add(restaurant);
        return restaurant;
    }

    public boolean remove(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }
        return restaurants.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant findById(Long restaurantId) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getId().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }
}
