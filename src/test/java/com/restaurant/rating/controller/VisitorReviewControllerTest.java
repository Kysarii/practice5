package com.restaurant.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.service.VisitorReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
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

@WebMvcTest(VisitorReviewController.class)
class VisitorReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitorReviewService visitorReviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private VisitorReviewRequestDTO requestDTO;
    private VisitorReviewResponseDTO responseDTO;
    private Restaurant restaurant;
    private final Long visitorId = 1L;
    private final Long restaurantId = 1L;

    @BeforeEach
    void setUp() {
        requestDTO = new VisitorReviewRequestDTO(visitorId, restaurantId, 5, "Отличное место");
        responseDTO = new VisitorReviewResponseDTO(visitorId, restaurantId, 5, "Отличное место");
        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Тестовый ресторан");
        restaurant.setUserRating(BigDecimal.valueOf(2.0));
    }

    @Test
    void findAllVisitorReviewsShouldReturnPagedReviews() throws Exception{
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rating"));
        List<VisitorReviewResponseDTO> reviews = Collections.singletonList(responseDTO);
        Page<VisitorReviewResponseDTO> page = new PageImpl<>(reviews, pageable, reviews.size());

        when(visitorReviewService.findAllVisitorReview(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "rating,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].visitorId").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].restaurantId").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].review").value("Отличное место"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1));
    }

    @Test
    void findAllVisitorReviewsShouldReturnEmptyPageWhenNoReviews() throws Exception{
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rating"));
        Page<VisitorReviewResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(visitorReviewService.findAllVisitorReview(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "rating,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0));
    }

    @Test
    void findVisitorReviewByIdShouldReturnReviewWhenExists() throws Exception{
        when(visitorReviewService.findVisitorReviewById(visitorId, restaurantId)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visitorId").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.review").value("Отличное место"));
    }

    @Test
    void findVisitorReviewByIdShouldReturnNotFoundWhenNotExists() throws Exception{
        when(visitorReviewService.findVisitorReviewById(visitorId, restaurantId))
                .thenThrow(new NoSuchElementException("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createVisitorReviewShouldReturnCreatedReviewWhenValidRequest() throws Exception{
        when(visitorReviewService.saveVisitorReview(any(VisitorReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visitorId").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.review").value("Отличное место"));
    }

    @Test
    void createVisitorReviewShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        VisitorReviewRequestDTO invalidDTO = new VisitorReviewRequestDTO(visitorId, restaurantId, -1, "");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVisitorReviewShouldReturnUpdatedReviewWhenValidRequest() throws Exception{
        when(visitorReviewService.updateVisitorReviewById(eq(visitorId), eq(restaurantId), any(VisitorReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.visitorId").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.review").value("Отличное место"));
    }

    @Test
    void updateVisitorReviewShouldReturnNotFoundWhenReviewNotExists() throws Exception{
        when(visitorReviewService.updateVisitorReviewById(eq(visitorId), eq(restaurantId), any(VisitorReviewRequestDTO.class)))
                .thenThrow(new NoSuchElementException("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateVisitorReviewShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        VisitorReviewRequestDTO invalidDTO = new VisitorReviewRequestDTO(visitorId, restaurantId, -1, "");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteVisitorReviewShouldReturnNoContentWhenSuccessful() throws Exception{
        when(visitorReviewService.removeVisitorReview(visitorId, restaurantId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteVisitorReviewShouldReturnNotFoundWhenReviewNotExists() throws Exception{
        when(visitorReviewService.removeVisitorReview(visitorId, restaurantId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reviews/visitor/{visitorId}/restaurant/{restaurantId}", visitorId, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findRestaurantsByMinRatingShouldReturnListOfRestaurants() throws Exception{
        BigDecimal minRating = BigDecimal.valueOf(4.0);
        List<Restaurant> restaurants = Collections.singletonList(restaurant);

        when(visitorReviewService.findRestaurantsByMinRating(minRating)).thenReturn(restaurants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/restaurants-by-min-rating")
                        .param("minRating", "4.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(restaurantId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Тестовый ресторан"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userRating").value(2.0));
    }

    @Test
    void findRestaurantsByMinRatingShouldReturnEmptyListWhenNoMatches() throws Exception{
        BigDecimal minRating = BigDecimal.valueOf(4.0);

        when(visitorReviewService.findRestaurantsByMinRating(minRating)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/restaurants-by-min-rating")
                        .param("minRating", "4.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}