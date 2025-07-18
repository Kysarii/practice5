package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.entity.VisitorReviewId;
import com.restaurant.rating.mapper.VisitorReviewMapper;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VisitorReviewService {
    private final VisitorReviewRepo visitorReviewRepo;
    private final RestaurantService restaurantService;
    private final VisitorRepo visitorRepo;
    private final RestaurantRepo restaurantRepo;
    private final VisitorReviewMapper visitorReviewMapper;

    public VisitorReviewService(VisitorReviewRepo visitorReviewRepo, RestaurantService restaurantService, VisitorRepo visitorRepo, RestaurantRepo restaurantRepo, VisitorReviewMapper visitorReviewMapper) {
        this.visitorReviewRepo = visitorReviewRepo;
        this.restaurantService = restaurantService;
        this.visitorRepo = visitorRepo;
        this.restaurantRepo = restaurantRepo;
        this.visitorReviewMapper = visitorReviewMapper;
    }


    public VisitorReviewResponseDTO saveVisitorReview(VisitorReviewRequestDTO requestDTO) {
        Visitor visitor = visitorRepo.findById(requestDTO.visitorId())
                .orElseThrow(() -> new NoSuchElementException("Посетитель с id: " + requestDTO.visitorId() + " не найден"));
        Restaurant restaurant = restaurantRepo.findById(requestDTO.restaurantId())
                .orElseThrow(() -> new NoSuchElementException("Ресторан с id: " + requestDTO.restaurantId() + " не найден"));

        VisitorReviewId visitorReviewId = new VisitorReviewId(requestDTO.visitorId(), requestDTO.restaurantId());
        VisitorReview visitorReview = VisitorReview.builder()
                .id(visitorReviewId)
                .visitor(visitor)
                .restaurant(restaurant)
                .rating(requestDTO.rating())
                .review(requestDTO.review())
                .build();

        VisitorReview updated = visitorReviewRepo.save(visitorReview);
        restaurantService.recalculateRating(updated.getId().getRestaurantId());
        return visitorReviewMapper.toDto(updated);
    }

    public boolean removeVisitorReview(Long visitorId, Long restaurantId) {
        VisitorReviewId visitorReviewId = new VisitorReviewId(visitorId, restaurantId);
        if (visitorReviewRepo.existsById(visitorReviewId)) {
            visitorReviewRepo.deleteById(visitorReviewId);
            restaurantService.recalculateRating(restaurantId);
            return true;
        }
        return false;
    }

    public List<VisitorReviewResponseDTO> findAllVisitorReview(){
        List<VisitorReview> visitorReviews = visitorReviewRepo.findAll();
        return  visitorReviewMapper.toDtoList(visitorReviews);
    }

    public VisitorReviewResponseDTO findVisitorReviewById(Long visitorId, Long restaurantId) {
        VisitorReviewId visitorReviewId = new VisitorReviewId(visitorId, restaurantId);
        VisitorReview visitorReview = visitorReviewRepo.findById(visitorReviewId)
                .orElseThrow(() -> new NoSuchElementException("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден"));
        return visitorReviewMapper.toDto(visitorReview);
    }

    public VisitorReviewResponseDTO updateVisitorReviewById(Long visitorId, Long restaurantId, VisitorReviewRequestDTO requestDTO) {
        VisitorReviewId visitorReviewId = new VisitorReviewId(visitorId, restaurantId);
        VisitorReview exist = visitorReviewRepo.findById(visitorReviewId)
                .orElseThrow(() -> new NoSuchElementException("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден"));
        exist.setRating(requestDTO.rating());
        exist.setReview(requestDTO.review());

        VisitorReview  updated = visitorReviewRepo.save(exist);
        return visitorReviewMapper.toDto(updated);
    }

}
