package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByUserRatingGreaterThanEqual(BigDecimal minRating);

    @Query("SELECT r FROM Restaurant r WHERE r.userRating >= :minRating")
    List<Restaurant> findRestaurantsWithRatingGreaterThanEqual(@Param("minRating") BigDecimal minRating);
}
