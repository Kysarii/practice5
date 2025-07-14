package com.restaurant.rating.config;

import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.enums.Gender;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.service.RestaurantService;
import com.restaurant.rating.service.VisitorReviewService;
import com.restaurant.rating.service.VisitorService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer {
    private final RestaurantService restaurantService;
    private final VisitorService visitorService;
    private final VisitorReviewService visitorReviewService;

    public DataInitializer(RestaurantService restaurantService, VisitorService visitorService, VisitorReviewService visitorReviewService) {
        this.restaurantService = restaurantService;
        this.visitorService = visitorService;
        this.visitorReviewService = visitorReviewService;
    }

    @PostConstruct
    public void init() {
        Restaurant restaurant1 = Restaurant.builder()
                .restaurantName("Вкусно - и точка")
                .description("Фастфуд")
                .kitchenType(KitchenType.AMERICAN)
                .averageCheck(700.0)
                .userRating(BigDecimal.ZERO)
                .build();
        restaurantService.saveRestaurant(restaurant1);

        Restaurant restaurant2 = Restaurant.builder()
                .restaurantName("Restaurant Mont Blanc")
                .kitchenType(KitchenType.EUROPEAN)
                .averageCheck(950.0)
                .userRating(BigDecimal.ZERO)
                .build();
        restaurantService.saveRestaurant(restaurant2);

        Restaurant restaurant3 = Restaurant.builder()
                .restaurantName("Шаурма бистро")
                .description("Быстрое питание")
                .kitchenType(KitchenType.RUSSIAN)
                .averageCheck(300.0)
                .userRating(BigDecimal.ZERO)
                .build();
        restaurantService.saveRestaurant(restaurant3);

        Visitor visitor1 = Visitor.builder()
                .name("Геннадий")
                .age(26)
                .gender(Gender.MALE)
                .build();
        visitorService.saveVisitor(visitor1);

        Visitor visitor2 = Visitor.builder()
                .name("Мария")
                .age(19)
                .gender(Gender.FEMALE)
                .build();
        visitorService.saveVisitor(visitor2);

        Visitor visitor3 = Visitor.builder()
                .age(43)
                .gender(Gender.FEMALE)
                .build();
        visitorService.saveVisitor(visitor3);

        VisitorReview visitorReview1 = VisitorReview.builder()
                .restaurantId(restaurant1.getId())
                .rating(4)
                .review("Котлета успела остыть")
                .build();
        visitorReviewService.saveVisitorReview(visitorReview1);

        VisitorReview visitorReview2 = VisitorReview.builder()
                .restaurantId(restaurant2.getId())
                .rating(5)
                .review("Все было шикарно")
                .build();
        visitorReviewService.saveVisitorReview(visitorReview2);

        VisitorReview visitorReview3 = VisitorReview.builder()
                .restaurantId(restaurant3.getId())
                .rating(3)
                .build();
        visitorReviewService.saveVisitorReview(visitorReview3);

        VisitorReview visitorReview4 = VisitorReview.builder()
                .restaurantId(restaurant3.getId())
                .rating(5)
                .review("Вкуснейшая шаурма")
                .build();
        visitorReviewService.saveVisitorReview(visitorReview4);

        System.out.println("---Данные инициализированны с помощью @PostConstruct---");
    }

}
