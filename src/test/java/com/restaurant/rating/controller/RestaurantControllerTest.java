package com.restaurant.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private RestaurantRequestDTO requestDTO;
    private RestaurantResponseDTO responseDTO;
    private final Long restaurantId = 1L;

    @BeforeEach
    void setUp() {
        requestDTO = new RestaurantRequestDTO("Тестовый ресторан", "Описание", KitchenType.AMERICAN, 900.0, BigDecimal.valueOf(0.0));
        responseDTO = new RestaurantResponseDTO(restaurantId, "Тестовый ресторан", "Описание", KitchenType.AMERICAN, 900.0, BigDecimal.valueOf(0.0));
    }

    @Test
    void findAllRestaurantsShouldReturnListOfRestaurants() throws Exception{
        List<RestaurantResponseDTO> restaurants = Collections.singletonList(responseDTO);

        when(restaurantService.findAllRestaurant()).thenReturn(restaurants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Тестовый ресторан"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].kitchenType").value(KitchenType.AMERICAN.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].averageCheck").value(900.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userRating").value(0.0));
    }

    @Test
    void findAllRestaurantsShouldReturnEmptyListWhenNoRestaurants() throws Exception{
        when(restaurantService.findAllRestaurant()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void getRestaurantByIdShouldReturnRestaurantWhenExists() throws Exception{
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Тестовый ресторан"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kitchenType").value(KitchenType.AMERICAN.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.averageCheck").value(900.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRating").value(0.0));
    }

    @Test
    void getRestaurantByIdShouldReturnNotFoundWhenNotExists() throws Exception{
        String errorMessage = "Ресторан с id: " + restaurantId + " не найден";
        when(restaurantService.getRestaurantById(restaurantId)).thenThrow(new NoSuchElementException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    @Test
    void createRestaurantShouldReturnCreatedRestaurantWhenValidRequest() throws Exception{
        when(restaurantService.saveRestaurant(any(RestaurantRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Тестовый ресторан"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kitchenType").value(KitchenType.AMERICAN.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.averageCheck").value(900.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRating").value(0.0));
    }

    @Test
    void createRestaurantShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        RestaurantRequestDTO invalidDTO = new RestaurantRequestDTO("", "", KitchenType.AMERICAN, -1.0, BigDecimal.valueOf(-1));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRestaurantShouldReturnUpdatedRestaurantWhenValidRequest() throws Exception{
        when(restaurantService.updateRestaurantById(eq(restaurantId), any(RestaurantRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Тестовый ресторан"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kitchenType").value(KitchenType.AMERICAN.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.averageCheck").value(900.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userRating").value(0.0));
    }

    @Test
    void updateRestaurantShouldReturnNotFoundWhenRestaurantNotExists() throws Exception{
        String errorMessage = "Ресторан с id: " + restaurantId + " не найден";
        when(restaurantService.updateRestaurantById(eq(restaurantId), any(RestaurantRequestDTO.class)))
                .thenThrow(new NoSuchElementException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    @Test
    void updateRestaurantShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        RestaurantRequestDTO invalidDTO = new RestaurantRequestDTO("", "", KitchenType.AMERICAN, -1.0, BigDecimal.valueOf(-1));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRestaurantShouldReturnNoContentWhenSuccessful() throws Exception{
        when(restaurantService.removeRestaurantById(restaurantId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRestaurantShouldReturnNotFoundWhenRestaurantNotExists() throws Exception{
        String errorMessage = "Ресторан с id: " + restaurantId + " не найден";
        when(restaurantService.removeRestaurantById(restaurantId)).thenThrow(new NoSuchElementException(errorMessage));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));

    }

}