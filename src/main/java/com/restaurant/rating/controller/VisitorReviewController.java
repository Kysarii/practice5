package com.restaurant.rating.controller;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.service.VisitorReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Управление отзывами посетителей", description = "API для управления отзывами посетителей")
@Validated
public class VisitorReviewController {

    private final VisitorReviewService visitorReviewService;

    public VisitorReviewController(VisitorReviewService visitorReviewService) {
        this.visitorReviewService = visitorReviewService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы с пагинацией и сортировкой",
            description = "Возвращает все отзывы с пагинацией, по умолчанию сначала самые высокие оценки")
    public Page<VisitorReviewResponseDTO> findAllVisitorReviews(@PageableDefault(sort = "rating", direction = Sort.Direction.DESC) Pageable pageable ) {
        return visitorReviewService.findAllVisitorReview(pageable);
    }

    @GetMapping("/visitor/{visitorId}/restaurant/{restaurantId}")
    @Operation(summary = "Получить отзыв по ID", description = "Возвращает информацию об отзыве по его ID")
    public VisitorReviewResponseDTO findVisitorReviewById(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        return visitorReviewService.findVisitorReviewById(visitorId, restaurantId);
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв", description = "Создает новый отзыв посетителя с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorReviewResponseDTO createVisitorReview(@Valid @RequestBody VisitorReviewRequestDTO requestDTO) {
        return visitorReviewService.saveVisitorReview(requestDTO);
    }

    @PutMapping("/visitor/{visitorId}/restaurant/{restaurantId}")
    @Operation(summary = "Обновить отзыв", description = "Обновляет информацию об отзыве по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorReviewResponseDTO updateVisitorReview(@PathVariable Long visitorId, @PathVariable Long restaurantId,
                                                        @Valid @RequestBody VisitorReviewRequestDTO requestDTO) {
        return visitorReviewService.updateVisitorReviewById(visitorId, restaurantId, requestDTO);
    }

    @DeleteMapping("/visitor/{visitorId}/restaurant/{restaurantId}")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв по его ID")
    public void deleteVisitorReviewById(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        visitorReviewService.removeVisitorReview(visitorId, restaurantId);
    }

    @GetMapping("/restaurants-by-min-rating")
    @Operation(summary = "Найти рестораны с рейтингом не меньше заданного",
            description = "Возвращает список ресторанов, у которых рейтинг не меньше указанного значения")
    public List<Restaurant> findRestaurantsByMinRating(@RequestParam BigDecimal minRating) {
        return visitorReviewService.findRestaurantsByMinRating(minRating);
    }

}
