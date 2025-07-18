package com.restaurant.rating.repository;

import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.entity.VisitorReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorReviewRepo extends JpaRepository<VisitorReview, VisitorReviewId> {
    List<VisitorReview> findByRestaurantId(Long restaurantId);
}
