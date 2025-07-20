package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.entity.VisitorReviewId;
import com.restaurant.rating.mapper.VisitorReviewMapper;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorReviewServiceTest {
    @Mock
    private VisitorReviewRepo visitorReviewRepo;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private VisitorRepo visitorRepo;

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private VisitorReviewMapper visitorReviewMapper;

    @InjectMocks
    private VisitorReviewService visitorReviewService;

    private Visitor visitor;
    private Restaurant restaurant;
    private VisitorReview visitorReview;
    private VisitorReviewRequestDTO requestDTO;
    private VisitorReviewResponseDTO responseDTO;
    private VisitorReviewId visitorReviewId;
    private final Long visitorId = 1L;
    private final Long restaurantId = 1L;

    @BeforeEach
    void setUp() {
        requestDTO = new VisitorReviewRequestDTO(visitorId, restaurantId, 5, "Отличный ресторан");
        visitor = new Visitor();
        visitor.setId(visitorId);
        restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        visitorReviewId = new VisitorReviewId(visitorId, restaurantId);

        visitorReview = VisitorReview.builder()
                .id(visitorReviewId)
                .visitor(visitor)
                .restaurant(restaurant)
                .rating(5)
                .review("Great place!")
                .build();

        responseDTO = new VisitorReviewResponseDTO(visitorId, restaurantId, 5, "Отличный ресторан");
    }

    @Test
    void saveVisitorReviewShouldSaveRecalculateAndReturnDtoWhenEntitiesExist() {
        when(visitorRepo.findById(visitorId)).thenReturn(Optional.of(visitor));
        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(visitorReviewRepo.save(any(VisitorReview.class))).thenReturn(visitorReview);
        when(restaurantService.recalculateRating(restaurantId)).thenReturn(BigDecimal.valueOf(5.0));
        when(visitorReviewMapper.toDto(visitorReview)).thenReturn(responseDTO);

        VisitorReviewResponseDTO result = visitorReviewService.saveVisitorReview(requestDTO);
        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void saveVisitorReviewShouldThrowExceptionWhenVisitorNotFound(){
        when(visitorRepo.findById(visitorId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorReviewService.saveVisitorReview(requestDTO));

        assertEquals("Посетитель с id: " + visitorId + " не найден", exception.getMessage());
    }

    @Test
    void saveVisitorReviewShouldThrowExceptionWhenRestaurantNotFound(){
        when(visitorRepo.findById(visitorId)).thenReturn(Optional.of(visitor));
        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorReviewService.saveVisitorReview(requestDTO));

        assertEquals("Ресторан с id: " + restaurantId + " не найден", exception.getMessage());
    }

    @Test
    void removeVisitorReviewShouldRemoveAndRecalculateWhenExists(){
        when(visitorReviewRepo.existsById(visitorReviewId)).thenReturn(true);
        when(restaurantService.recalculateRating(restaurantId)).thenReturn(BigDecimal.valueOf(4.0));

        boolean result = visitorReviewService.removeVisitorReview(visitorId, restaurantId);

        assertTrue(result);
    }

    @Test
    void removeVisitorReviewShouldReturnFalseWhenNotExists(){
        when(visitorReviewRepo.existsById(visitorReviewId)).thenReturn(false);

        boolean result = visitorReviewService.removeVisitorReview(visitorId, restaurantId);

        assertFalse(result);
    }

    @Test
    void findAllVisitorReviewShouldReturnPageOfDTO(){
        Pageable pageable = PageRequest.of(0, 10);
        List<VisitorReview> reviews = Collections.singletonList(visitorReview);
        Page<VisitorReview> page = new PageImpl<>(reviews, pageable, reviews.size());

        when(visitorReviewRepo.findAll(pageable)).thenReturn(page);
        when(visitorReviewMapper.toDto(visitorReview)).thenReturn(responseDTO);

        Page<VisitorReviewResponseDTO> result = visitorReviewService.findAllVisitorReview(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findAllVisitorReviewShouldReturnEmptyPageWhenNoReviews(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitorReview> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(visitorReviewRepo.findAll(pageable)).thenReturn(emptyPage);

        Page<VisitorReviewResponseDTO> result = visitorReviewService.findAllVisitorReview(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findVisitorReviewByIdShouldReturnDtoWhenExists(){
        when(visitorReviewRepo.findById(visitorReviewId)).thenReturn(Optional.of(visitorReview));
        when(visitorReviewMapper.toDto(visitorReview)).thenReturn(responseDTO);

        VisitorReviewResponseDTO result = visitorReviewService.findVisitorReviewById(visitorId, restaurantId);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void findVisitorReviewByIdShouldThrowExceptionWhenNotExists(){
        when(visitorReviewRepo.findById(visitorReviewId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorReviewService.findVisitorReviewById(visitorId, restaurantId));

        assertEquals("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден", exception.getMessage());
        verify(visitorReviewRepo, times(1)).findById(visitorReviewId);
    }

    @Test
    void updateVisitorReviewByIdShouldUpdateAndReturnDtoWhenExists(){
        VisitorReviewRequestDTO updateDTO = new VisitorReviewRequestDTO(visitorId, restaurantId, 3, "Обновленный отзыв");

        when(visitorReviewRepo.findById(visitorReviewId)).thenReturn(Optional.of(visitorReview));
        when(visitorReviewRepo.save(visitorReview)).thenReturn(visitorReview);
        when(visitorReviewMapper.toDto(visitorReview)).thenReturn(responseDTO);

        VisitorReviewResponseDTO result = visitorReviewService.updateVisitorReviewById(visitorId, restaurantId, updateDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        assertEquals(3, visitorReview.getRating());
        assertEquals("Обновленный отзыв", visitorReview.getReview());
    }

    @Test
    void updateVisitorReviewByIdShouldThrowExceptionWhenNotExists(){
        when(visitorReviewRepo.findById(visitorReviewId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorReviewService.updateVisitorReviewById(visitorId, restaurantId, requestDTO));

        assertEquals("Отзыв с visitorId: " + visitorId + " и restaurantId: " + restaurantId + " не найден", exception.getMessage());
    }

    @Test
    void findRestaurantsByMinRatingShouldReturnListOfRestaurants(){
        BigDecimal minRating = BigDecimal.valueOf(2.0);
        List<Restaurant> restaurants = Collections.singletonList(restaurant);

        when(restaurantRepo.findByUserRatingGreaterThanEqual(minRating)).thenReturn(restaurants);

        List<Restaurant> result = visitorReviewService.findRestaurantsByMinRating(minRating);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurants, result);
        verify(restaurantRepo, times(1)).findByUserRatingGreaterThanEqual(minRating);
    }

    @Test
    void findRestaurantsByMinRatingShouldReturnEmptyListWhenNoMatches(){
        BigDecimal minRating = BigDecimal.valueOf(1.0);

        when(restaurantRepo.findByUserRatingGreaterThanEqual(minRating)).thenReturn(Collections.emptyList());

        List<Restaurant> result = visitorReviewService.findRestaurantsByMinRating(minRating);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}