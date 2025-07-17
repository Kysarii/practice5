package com.restaurant.rating.controller;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Управление ресторанами", description = "API для управления ресторанами")
@Validated
public class RestaurantController {

    private  final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "Получить все рестораны", description = "Возвращает список всех ресторанов")
    public List<RestaurantResponseDTO> findAllRestaurants() {
        return restaurantService.findAllRestaurant();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ресторан по ID", description = "Возвращает информацию о ресторане по его ID")
    public RestaurantResponseDTO getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый ресторан", description = "Создает новый ресторан с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public RestaurantResponseDTO createRestaurant(@Valid @RequestBody RestaurantRequestDTO requestDTO) {
        return restaurantService.saveRestaurant(requestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные ресторана", description = "Обновляет информацию о ресторане по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public RestaurantResponseDTO updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDTO requestDTO) {
        return  restaurantService.updateRestaurantById(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан", description = "Удаляет ресторан по его ID")
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.removeRestaurantById(id);
    }

}
