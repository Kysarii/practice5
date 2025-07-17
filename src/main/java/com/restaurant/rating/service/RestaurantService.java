package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.mapper.RestaurantMapper;
import com.restaurant.rating.repository.RestaurantRepository;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final VisitorReviewRepository visitorReviewRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, VisitorReviewRepository visitorReviewRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.visitorReviewRepository = visitorReviewRepository;
        this.restaurantMapper = restaurantMapper;
    }

    public RestaurantResponseDTO saveRestaurant(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(saved);
    }

    public boolean removeRestaurantById(Long id) {
        return restaurantRepository.removeById(id);
    }

    public List<RestaurantResponseDTO> findAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurantMapper.toDTOList(restaurants);
    }

    public BigDecimal recalculateRating(Long restaurantId) {
        List<VisitorReview> visitorReviews = visitorReviewRepository.findByRestaurantId(restaurantId);

        BigDecimal newRating;
        if (visitorReviews.isEmpty()) {
            newRating = BigDecimal.ZERO;
        } else {
            newRating = BigDecimal.valueOf(visitorReviews.stream()
                    .mapToInt(VisitorReview::getRating)
                    .average()
                    .orElse(0.0));
        }
        updateRating(restaurantId, newRating);
        return newRating;
    }

    public RestaurantResponseDTO updateRating(Long restaurantId, BigDecimal newRating) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        restaurant.setUserRating(newRating);
        Restaurant updated = restaurantRepository.updateRestaurant(restaurantId, restaurant);
        return restaurantMapper.toDto(updated);
    }

    public Restaurant findRestaurantById(Restaurant restaurant) {
        return restaurantRepository.findById(restaurant.getId());
    }

    public RestaurantResponseDTO  getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponseDTO updateRestaurantById(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant updatedRestaurant = restaurantMapper.toEntity(requestDTO);
        Restaurant result = restaurantRepository.updateRestaurant(id, updatedRestaurant);
        return restaurantMapper.toDto(result);
    }

}
