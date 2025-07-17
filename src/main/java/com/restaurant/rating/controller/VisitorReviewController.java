package com.restaurant.rating.controller;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.service.VisitorReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Получить все отзывы", description = "Возвращает список всех отзывов посетителей")
    public List<VisitorReviewResponseDTO> findAllVisitorReviews() {
        return visitorReviewService.findAllVisitorReview();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID", description = "Возвращает информацию об отзыве по его ID")
    public VisitorReviewResponseDTO findVisitorReviewById(@PathVariable Long id) {
        return visitorReviewService.findVisitorReviewById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв", description = "Создает новый отзыв посетителя с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorReviewResponseDTO createVisitorReview(@Valid @RequestBody VisitorReviewRequestDTO requestDTO) {
        return visitorReviewService.saveVisitorReview(requestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв", description = "Обновляет информацию об отзыве по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorReviewResponseDTO updateVisitorReview(@PathVariable Long id,@Valid @RequestBody VisitorReviewRequestDTO requestDTO) {
        return visitorReviewService.updateVisitorReviewById(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв по его ID")
    public void deleteVisitorReviewById(@PathVariable Long id) {
        visitorReviewService.removeVisitorReview(id);
    }

}
