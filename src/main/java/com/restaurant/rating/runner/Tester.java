package com.restaurant.rating.runner;

import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.service.RestaurantService;
import com.restaurant.rating.service.VisitorReviewService;
import com.restaurant.rating.service.VisitorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.List;

@Component
@Log4j2
public class Tester implements CommandLineRunner {

    private final RestaurantService restaurantService;
    private final VisitorService visitorService;
    private final VisitorReviewService visitorReviewService;

    public Tester(RestaurantService restaurantService, VisitorService visitorService, VisitorReviewService visitorReviewService) {
        this.restaurantService = restaurantService;
        this.visitorService = visitorService;
        this.visitorReviewService = visitorReviewService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("---Тестирование с помощью CommandLineRunner---");

        // Тест 1
        List<Restaurant> restaurantList = restaurantService.findAllRestaurant();
        log.info("Всего ресторанов: {}", restaurantList.size());
        restaurantList.forEach(r -> log.info(" - {} (Оценка: {})", r.getRestaurantName(), r.getUserRating()));

        // Тест 2
        List<Visitor> allVisitors = visitorService.findAllVisitors();
        log.info("Все посетители: {}", allVisitors.size());
        allVisitors.forEach(v -> log.info(" - {}", v.getName()));

        // Тест 3
        List<VisitorReview> visitorReviewList = visitorReviewService.findAllVisitorReview();
        log.info("Все отзывы: {}", visitorReviewList.size());
        visitorReviewList.forEach(rev -> log.info(" - Review ID: {}, Restaurant ID: {}, Rating: {}",
                rev.getId(), rev.getRestaurantId(), rev.getRating()));

        // Тест 4
        Restaurant newRestaurant = Restaurant.builder()
                .restaurantName("Sintoho")
                .description("Китайская еда")
                .kitchenType(KitchenType.CHINESE)
                .averageCheck(650.0)
                .userRating(BigDecimal.ZERO)
                .build();
        restaurantService.saveRestaurant(newRestaurant);
        List<Restaurant> restaurantList1 = restaurantService.findAllRestaurant();
        log.info("Добавлен новый ресторан: {}", newRestaurant.getRestaurantName());
        log.info("Всего ресторанов: {}", restaurantList1.size());
        restaurantList1.forEach(r -> log.info(" - {} (Оценка: {})", r.getRestaurantName(), r.getUserRating()));

        // Тест 5
        VisitorReview newReview = VisitorReview.builder()
                .restaurantId(newRestaurant.getId())
                .rating(3)
                .review("Крайне острые супы")
                .build();
        visitorReviewService.saveVisitorReview(newReview);
        List<VisitorReview> visitorReviewList1 = visitorReviewService.findAllVisitorReview();
        log.info("Все отзывы: {}", visitorReviewList1.size());
        visitorReviewList1.forEach(rev -> log.info(" - Review ID: {}, Restaurant ID: {}, Rating: {}",
                rev.getId(), rev.getRestaurantId(), rev.getRating()));
        List<Restaurant> restaurantList2 = restaurantService.findAllRestaurant();
        log.info("Всего ресторанов: {}", restaurantList2.size());
        restaurantList2.forEach(r -> log.info(" - {} (Оценка: {})", r.getRestaurantName(), r.getUserRating()));

        // Тест 6
        restaurantService.removeRestaurant(newRestaurant);
        List<Restaurant> restaurantList3 = restaurantService.findAllRestaurant();
        log.info("Удален новый ресторан: {}", newRestaurant.getRestaurantName());
        log.info("Всего ресторанов: {}", restaurantList3.size());
        restaurantList3.forEach(r -> log.info(" - {} (Оценка: {})", r.getRestaurantName(), r.getUserRating()));

        log.info("--- Проверка завершена ---");
    }
}
