package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();

    public Restaurant save(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Ресторан не может быть null");
        }
        if (restaurants.contains(restaurant)) {
            throw new IllegalStateException("Ресторан с таким id уже существует");
        }
        if (restaurants.isEmpty()) {
            restaurant.setId(0L);
        } else {
            restaurant.setId(restaurants.getLast().getId() + 1);
        }

        restaurants.add(restaurant);
        return restaurant;
    }

    public boolean removeById(Long id) {
        Restaurant restaurantToRemove = findById(id);
        if (restaurantToRemove != null) {
            return restaurants.remove(restaurantToRemove);
        }
        return false;
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

    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant oldRestaurant = findById(id);
        if (oldRestaurant == null) {
            throw new IllegalArgumentException("Ресторан с id:  " + id + " не найден");
        }
        oldRestaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
        oldRestaurant.setDescription(updatedRestaurant.getDescription());
        oldRestaurant.setKitchenType(updatedRestaurant.getKitchenType());
        oldRestaurant.setAverageCheck(updatedRestaurant.getAverageCheck());
        oldRestaurant.setUserRating(updatedRestaurant.getUserRating());

        return oldRestaurant;
    }
}
