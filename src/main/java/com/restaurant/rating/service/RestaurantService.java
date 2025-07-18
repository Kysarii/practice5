package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.mapper.RestaurantMapper;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RestaurantService {
    private final RestaurantRepo restaurantRepo;
    private final VisitorReviewRepo visitorReviewRepo;
    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepo restaurantRepo, VisitorReviewRepo visitorReviewRepo, RestaurantMapper restaurantMapper) {
        this.restaurantRepo = restaurantRepo;
        this.visitorReviewRepo = visitorReviewRepo;
        this.restaurantMapper = restaurantMapper;
    }


    public RestaurantResponseDTO saveRestaurant(RestaurantRequestDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        Restaurant saved = restaurantRepo.save(restaurant);
        return restaurantMapper.toDto(saved);
    }

    public boolean removeRestaurantById(Long id) {
        if (restaurantRepo.existsById(id)) {
            restaurantRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RestaurantResponseDTO> findAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepo.findAll();
        return restaurantMapper.toDTOList(restaurants);
    }

    public BigDecimal recalculateRating(Long restaurantId) {
        List<VisitorReview> visitorReviews = visitorReviewRepo.findByRestaurantId(restaurantId);

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
        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException("Ресторан с id: " + restaurantId + " не найден"));;
        restaurant.setUserRating(newRating);
        Restaurant updated = restaurantRepo.save(restaurant);
        return restaurantMapper.toDto(updated);
    }

    public RestaurantResponseDTO  getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ресторан с id: " + id + " не найден"));
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponseDTO updateRestaurantById(Long id, RestaurantRequestDTO requestDTO) {
        Restaurant exist = restaurantRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ресторан с id: " + id + " не найден"));
        exist.setName(requestDTO.name());
        exist.setDescription(requestDTO.description());
        exist.setKitchenType(requestDTO.kitchenType());
        exist.setAverageCheck(requestDTO.averageCheck());
        exist.setUserRating(requestDTO.userRating());

        Restaurant updated = restaurantRepo.save(exist);
        return restaurantMapper.toDto(updated);
    }

}
