package com.restaurant.rating.repository;

import com.restaurant.rating.entity.VisitorReview;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class VisitorReviewRepository {
    private final List<VisitorReview> reviews = new ArrayList<>();

    public VisitorReview save(VisitorReview review) {
        if (review == null) {
            throw new IllegalArgumentException("Отзыв не может быть null");
        }

        if (reviews.contains(review)) {
            throw new IllegalStateException("Отзыв уже существует");
        }

        if (reviews.isEmpty()) {
            review.setId(0L);
        } else {
            review.setId(reviews.getLast().getId() + 1);
        }

        reviews.add(review);
        return review;
    }

    public boolean remove(VisitorReview review) {
        if (review == null) {
            return false;
        }
        return reviews.remove(review);
    }

    public List<VisitorReview> findAll(){
        return new ArrayList<>(reviews);
    }

    public VisitorReview findById(Long id){
        return reviews.stream()
                .filter(review -> review.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<VisitorReview> findByRestaurantId(Long restaurantId){
        return reviews.stream()
                .filter(review -> review.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

}
