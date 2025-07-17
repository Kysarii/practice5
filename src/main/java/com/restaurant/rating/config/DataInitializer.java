package com.restaurant.rating.config;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.request.VisitorRequestDTO;
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
        RestaurantRequestDTO restaurant1Dto = new RestaurantRequestDTO(
                "Вкусно - и точка",
                "Фастфуд",
                KitchenType.AMERICAN,
                700.0,
                BigDecimal.ZERO
        );
        restaurantService.saveRestaurant(restaurant1Dto);

        RestaurantRequestDTO restaurant2Dto = new RestaurantRequestDTO(
                "Restaurant Mont Blanc",
                null,
                KitchenType.EUROPEAN,
                950.0,
                BigDecimal.ZERO
        );
        restaurantService.saveRestaurant(restaurant2Dto);

        RestaurantRequestDTO restaurant3Dto = new RestaurantRequestDTO(
                "Шаурма бистро",
                "Быстрое питание",
                KitchenType.RUSSIAN,
                300.0,
                BigDecimal.ZERO
        );
        restaurantService.saveRestaurant(restaurant3Dto);

        VisitorRequestDTO visitor1Dto = new VisitorRequestDTO(
                "Геннадий",
                26,
                Gender.MALE
        );
        visitorService.saveVisitor(visitor1Dto);

        VisitorRequestDTO visitor2Dto = new VisitorRequestDTO(
                "Мария",
                19,
                Gender.FEMALE
        );
        visitorService.saveVisitor(visitor2Dto);

        VisitorRequestDTO visitor3Dto = new VisitorRequestDTO(
                null,
                43,
                Gender.FEMALE
        );
        visitorService.saveVisitor(visitor3Dto);

        //VisitorReview visitorReview1 = VisitorReview.builder()
        //        .restaurantId(restaurant1Dto.id())
        //        .rating(4)
        //        .review("Котлета успела остыть")
        //        .build();
        //visitorReviewService.saveVisitorReview(visitorReview1);
//
        //VisitorReview visitorReview2 = VisitorReview.builder()
        //        .restaurantId(restaurant2Dto.id())
        //        .rating(5)
        //        .review("Все было шикарно")
        //        .build();
        //visitorReviewService.saveVisitorReview(visitorReview2);
//
        //VisitorReview visitorReview3 = VisitorReview.builder()
        //        .restaurantId(restaurant3Dto.id())
        //        .rating(3)
        //        .build();
        //visitorReviewService.saveVisitorReview(visitorReview3);
//
        //VisitorReview visitorReview4 = VisitorReview.builder()
        //        .restaurantId(restaurant3Dto.id())
        //        .rating(5)
        //        .review("Вкуснейшая шаурма")
        //        .build();
        //visitorReviewService.saveVisitorReview(visitorReview4);

        System.out.println("---Данные инициализированны с помощью @PostConstruct---");
    }

}
