package com.restaurant.rating.service;

import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.repository.RestaurantRepository;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final VisitorReviewRepository visitorReviewRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, VisitorReviewRepository visitorReviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.visitorReviewRepository = visitorReviewRepository;
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public boolean removeRestaurant(Restaurant restaurant) {
        return restaurantRepository.remove(restaurant);
    }

    public List<Restaurant> findAllRestaurant() {
        return restaurantRepository.findAll();
    }

    public BigDecimal recalculateRating(Long restaurantId) {
        List<VisitorReview> visitorReviews = visitorReviewRepository.findByRestaurantId(restaurantId);

        if (visitorReviews.isEmpty()) {
            BigDecimal newRating = BigDecimal.ZERO;
            updateRating(restaurantId, newRating);
            return newRating;
        }

        BigDecimal averageRating = BigDecimal.valueOf(visitorReviews.stream()
                .mapToInt(VisitorReview::getRating)
                .average()
                .orElse(0.0));

        updateRating(restaurantId, averageRating);

        return averageRating;
    }

    public Restaurant updateRating(Long restaurantId, BigDecimal newRating) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        restaurant.setUserRating(newRating);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant findRestaurantById(Restaurant restaurant) {
        return restaurantRepository.findById(restaurant.getId());
    }
}
