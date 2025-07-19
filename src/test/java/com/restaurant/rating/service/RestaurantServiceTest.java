package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.mapper.RestaurantMapper;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private VisitorReviewRepo visitorReviewRepo;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDTO requestDTO;
    private RestaurantResponseDTO responseDTO;
    private VisitorReview review1;
    private VisitorReview review2;

    @BeforeEach
    void setUp() {
        requestDTO = new RestaurantRequestDTO("Тестовый ресторан", "Описание", KitchenType.AMERICAN, 1500.0, BigDecimal.valueOf(0.0));
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Тестовый ресторан");
        restaurant.setDescription("Описание");
        restaurant.setKitchenType(KitchenType.AMERICAN);
        restaurant.setAverageCheck(1500.0);
        restaurant.setUserRating(BigDecimal.valueOf(0.0));

        responseDTO = new RestaurantResponseDTO(1L, "Тестовый ресторан", "Описание", KitchenType.AMERICAN, 1500.0, BigDecimal.valueOf(0.0));

        review1 = new VisitorReview();
        review1.setRating(3);
        review2 = new VisitorReview();
        review2.setRating(5);

    }

    @Test
    void saveRestaurantShouldSaveAndReturnDto() {
        when(restaurantMapper.toEntity(requestDTO)).thenReturn(restaurant);
        when(restaurantRepo.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDTO);

        RestaurantResponseDTO result = restaurantService.saveRestaurant(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void removeRestaurantByIdShouldRemoveAndReturnTrueWhenExists() {
        Long id = 1L;
        when(restaurantRepo.existsById(id)).thenReturn(true);

        boolean result = restaurantService.removeRestaurantById(id);

        assertTrue(result);
    }

    @Test
    void removeRestaurantByIdShouldRemoveAndReturnTrueNotExists() {
        Long id = 1L;
        when(restaurantRepo.existsById(id)).thenReturn(false);

        boolean result = restaurantService.removeRestaurantById(id);

        assertFalse(result);
    }

    @Test
    void findAllRestaurantShouldReturnListOfDto() {
        List<Restaurant> restaurantList = Collections.singletonList(restaurant);
        List<RestaurantResponseDTO> restaurantResponseDTOList = Collections.singletonList(responseDTO);
        when(restaurantRepo.findAll()).thenReturn(restaurantList);
        when(restaurantMapper.toDTOList(restaurantList)).thenReturn(restaurantResponseDTOList);

        List<RestaurantResponseDTO> result = restaurantService.findAllRestaurant();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurantResponseDTOList, result);
    }

    @Test
    void findAllRestaurantShouldReturnEmptyListOfDtoWhenNoRestaurant() {
        when(restaurantRepo.findAll()).thenReturn(Collections.emptyList());
        when(restaurantMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<RestaurantResponseDTO> result = restaurantService.findAllRestaurant();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void recalculateRatingShouldCalculateAverageAndUpdateWhenReviewsExist() {
        Long id = 1L;
        List<VisitorReview> reviews = List.of(review1, review2);
        BigDecimal expectedRating = BigDecimal.valueOf(4.0);

        when(visitorReviewRepo.findByRestaurantId(id)).thenReturn(reviews);
        when(restaurantRepo.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepo.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDTO);

        BigDecimal result = restaurantService.recalculateRating(id);
        assertEquals(expectedRating, result);
        assertEquals(expectedRating, restaurant.getUserRating());
    }

    @Test
    void recalculateRating_shouldThrowException_whenRestaurantNotFound(){
        Long id = 1L;

        when(visitorReviewRepo.findByRestaurantId(id)).thenReturn(Collections.emptyList());
        when(restaurantRepo.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> restaurantService.recalculateRating(id));
        assertEquals("Ресторан с id: " + id + " не найден", exception.getMessage());
    }

    @Test
    void updateRatingShouldUpdateAndReturnDtoWhenExists() {
        Long id = 1L;
        BigDecimal newRating = BigDecimal.valueOf(4.0);

        when(restaurantRepo.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepo.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDTO);

        RestaurantResponseDTO result = restaurantService.updateRating(id, newRating);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        assertEquals(newRating, restaurant.getUserRating());
    }

    @Test
    void updateRating_shouldThrowException_whenNotExists(){
        Long id = 1L;
        BigDecimal newRating = BigDecimal.valueOf(4.0);

        when(restaurantRepo.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> restaurantService.updateRating(id, newRating));

        assertEquals("Ресторан с id: " + id + " не найден", exception.getMessage());
    }

    @Test
    void getRestaurantByIdShouldReturnDtoWhenExists() {
        Long id = 1L;

        when(restaurantRepo.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDTO);

        RestaurantResponseDTO result = restaurantService.getRestaurantById(id);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void getRestaurantById_shouldThrowException_whenNotExists(){
        Long id = 1L;

        when(restaurantRepo.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> restaurantService.getRestaurantById(id));

        assertEquals("Ресторан с id: " + id + " не найден", exception.getMessage());
    }

    @Test
    void updateRestaurantByIdShouldUpdateAndReturnDtoWhenExists() {
        Long id = 1L;
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setId(id);
        updatedRestaurant.setName(requestDTO.name());
        updatedRestaurant.setDescription(requestDTO.description());
        updatedRestaurant.setKitchenType(requestDTO.kitchenType());
        updatedRestaurant.setAverageCheck(requestDTO.averageCheck());
        updatedRestaurant.setUserRating(requestDTO.userRating());

        RestaurantResponseDTO updatedDto = new RestaurantResponseDTO(id, requestDTO.name(), requestDTO.description(),
                requestDTO.kitchenType(), requestDTO.averageCheck(), requestDTO.userRating());

        when(restaurantRepo.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantRepo.save(restaurant)).thenReturn(updatedRestaurant);
        when(restaurantMapper.toDto(updatedRestaurant)).thenReturn(updatedDto);

        RestaurantResponseDTO result = restaurantService.updateRestaurantById(id, requestDTO);

        assertNotNull(result);
        assertEquals(updatedDto, result);
        assertEquals(requestDTO.name(), restaurant.getName());
        assertEquals(requestDTO.description(), restaurant.getDescription());
        assertEquals(requestDTO.kitchenType(), restaurant.getKitchenType());
        assertEquals(requestDTO.averageCheck(), restaurant.getAverageCheck());
        assertEquals(requestDTO.userRating(), restaurant.getUserRating());
    }

    @Test
    void updateRestaurantById_shouldThrowException_whenNotExists(){
        Long id = 1L;

        when(restaurantRepo.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> restaurantService.updateRestaurantById(id, requestDTO));

        assertEquals("Ресторан с id: " + id + " не найден", exception.getMessage());
    }
}