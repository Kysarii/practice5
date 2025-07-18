package com.restaurant.rating.runner;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.enums.Gender;
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

        //VisitorRequestDTO visitorRequestDTO = VisitorRequestDTO.builder()
                //        .name("visitor")
                //        .age(50)
                //        .gender(Gender.MALE)
                //        .build();
        //
        //VisitorResponseDTO visitorResponseDTO = visitorService.saveVisitor(visitorRequestDTO);
        //boolean visitorResponseDTO1 = visitorService.removeVisitorById(visitorResponseDTO.id());
        //
        //
        //log.info(visitorResponseDTO);
        //log.info(visitorResponseDTO1);


        // Тест 1
        //List<RestaurantResponseDTO> restaurantList = restaurantService.findAllRestaurant();
        //log.info("Всего ресторанов: {}", restaurantList.size());
        //restaurantList.forEach(r -> log.info(" - {} (Оценка: {})", r.restaurantName(), r.userRating()));

        // Тест 2
        //List<VisitorResponseDTO> allVisitors = visitorService.findAllVisitors();
        //log.info("Все посетители: {}", allVisitors.size());
        //allVisitors.forEach(v -> log.info(" - {}", v.name()));

        // Тест 3
        //List<VisitorReviewResponseDTO> visitorReviewList = visitorReviewService.findAllVisitorReview();
        //log.info("Все отзывы: {}", visitorReviewList.size());
        //visitorReviewList.forEach(rev -> log.info(" - Отызыв ID: {}, Ресторан ID: {}, Оценка: {}",
        //        rev.id(), rev.restaurantId(), rev.rating()));

        // Тест 4
        //RestaurantRequestDTO newRestaurant = new RestaurantRequestDTO(
        //        "Sintoho",
        //        "Китайская еда",
        //        KitchenType.CHINESE,
        //        650.0,
        //        BigDecimal.ZERO
        //);
        //restaurantService.saveRestaurant(newRestaurant);
//
        //List<RestaurantResponseDTO> restaurantList1 = restaurantService.findAllRestaurant();
        //log.info("Добавлен новый ресторан: {}", newRestaurant.restaurantName());
        //log.info("Всего ресторанов: {}", restaurantList1.size());
        //restaurantList1.forEach(r -> log.info(" - {} (Оценка: {})", r.restaurantName(), r.userRating()));

        //Тест 5
        //VisitorReviewRequestDTO newReview = new VisitorReviewRequestDTO(
        //        0L,
        //        0L,
        //        5,
        //        "pepe"
        //);
        //visitorReviewService.saveVisitorReview(newReview);
        //List<VisitorReviewResponseDTO> visitorReviewList1 = visitorReviewService.findAllVisitorReview();
        //log.info("Все отзывы: {}", visitorReviewList1.size());
        //visitorReviewList1.forEach(rev -> log.info(" - Отызыв ID: {}, Ресторан ID: {}, Оценка: {}",
        //        rev.id(), rev.restaurantId(), rev.rating()));
        //List<RestaurantResponseDTO> restaurantList2 = restaurantService.findAllRestaurant();
        //log.info("Всего ресторанов: {}", restaurantList2.size());
        //restaurantList2.forEach(r -> log.info(" - {} (Оценка: {})", r.restaurantName(), r.userRating()));

        //Тест 6
        //restaurantService.removeRestaurantById(newRestaurant.Id());
        //List<RestaurantResponseDTO> restaurantList3 = restaurantService.findAllRestaurant();
        //log.info("Удален новый ресторан: {}", newRestaurant.restaurantName());
        //log.info("Всего ресторанов: {}", restaurantList3.size());
        //restaurantList3.forEach(r -> log.info(" - {} (Оценка: {})", r.restaurantName(), r.userRating()));

        log.info("--- Проверка завершена ---");
    }
}
