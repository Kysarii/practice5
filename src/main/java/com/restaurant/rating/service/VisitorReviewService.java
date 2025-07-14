package com.restaurant.rating.service;

import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorReviewService {
    private final VisitorReviewRepository visitorReviewRepository;
    private final RestaurantService restaurantService;

    public VisitorReviewService(VisitorReviewRepository visitorReviewRepository, RestaurantService restaurantService) {
        this.visitorReviewRepository = visitorReviewRepository;
        this.restaurantService = restaurantService;
    }

    public VisitorReview saveVisitorReview(VisitorReview visitorReview) {
        VisitorReview savedVisitorReview = visitorReviewRepository.save(visitorReview);
        restaurantService.recalculateRating(savedVisitorReview.getRestaurantId());
        return savedVisitorReview;
    }

    public boolean removeVisitorReview(VisitorReview visitorReview) {
        return visitorReviewRepository.remove(visitorReview);
    }

    public List<VisitorReview> findAllVisitorReview(){
        return visitorReviewRepository.findAll();
    }

    public VisitorReview findVisitorReviewById(VisitorReview visitorReview) {
        return visitorReviewRepository.findById(visitorReview.getId());
    }
}
