package com.restaurant.rating.integrationtest;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.entity.VisitorReviewId;
import com.restaurant.rating.enums.Gender;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import com.restaurant.rating.service.RestaurantService;
import com.restaurant.rating.service.VisitorReviewService;
import com.restaurant.rating.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServiceIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private VisitorRepo visitorRepo;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Autowired
    private VisitorReviewRepo visitorReviewRepo;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private VisitorReviewService visitorReviewService;

    @BeforeEach
    void setUp() {
        visitorReviewRepo.deleteAll();
        visitorRepo.deleteAll();
        restaurantRepo.deleteAll();
    }

    @Nested
    class VisitorServiceTests{

        @Test
        void testSaveVisitor() {
            VisitorRequestDTO dto = new VisitorRequestDTO("Александр", 20, Gender.MALE);
            VisitorResponseDTO saved = visitorService.saveVisitor(dto);

            assertThat(saved.id()).isNotNull();
            assertThat(saved.name()).isEqualTo("Александр");
        }

        @Test
        void testRemoveVisitorById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());

            boolean removed = visitorService.removeVisitorById(visitor.getId());

            assertThat(removed).isTrue();
            assertThat(visitorRepo.findById(visitor.getId())).isEmpty();
        }

        @Test
        void testRemoveVisitorByIdNotFound() {
            boolean removed = visitorService.removeVisitorById(100L);

            assertThat(removed).isFalse();
        }

        @Test
        void testFindAllVisitors() {
            visitorRepo.saveAll(List.of(Visitor.builder()
                    .name("Анастасия")
                    .age(20)
                    .gender(Gender.FEMALE)
                    .build(),
                    Visitor.builder()
                            .name("Александр")
                            .age(20)
                            .gender(Gender.MALE)
                            .build()));
            List<VisitorResponseDTO> all = visitorService.findAllVisitors();

            assertThat(all).hasSize(2);
        }

        @Test
        void testGetVisitorById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            VisitorResponseDTO dto = visitorService.getVisitorById(visitor.getId());

            assertThat(dto.name()).isEqualTo("Александр");
        }

        @Test
        void testGetVisitorByIdNotFound() {
            assertThatThrownBy(() -> visitorService.getVisitorById(100L))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void testUpdateVisitorById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            VisitorRequestDTO updateDto = new VisitorRequestDTO("Обновленный Александр", 30, Gender.MALE);
            VisitorResponseDTO updated = visitorService.updateVisitorById(visitor.getId(), updateDto);

            assertThat(updated.name()).isEqualTo("Обновленный Александр");
            assertThat(updated.age()).isEqualTo(30);
        }
    }

    @Nested
    class RestaurantServiceTests{
        @Test
        void testSaveRestaurant() {
            RestaurantRequestDTO dto = new RestaurantRequestDTO("Пицца", "ПиццаХаус",
                    KitchenType.ITALIAN, 900.0, BigDecimal.valueOf(4.5));
            RestaurantResponseDTO saved = restaurantService.saveRestaurant(dto);

            assertThat(saved.id()).isNotNull();
            assertThat(saved.name()).isEqualTo("Пицца");
        }

        @Test
        void testRemoveRestaurantById() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build());
            boolean removed = restaurantService.removeRestaurantById(restaurant.getId());

            assertThat(removed).isTrue();
            assertThat(restaurantRepo.findById(restaurant.getId())).isEmpty();
        }

        @Test
        void testRemoveRestaurantByIdNotFound() {
            boolean removed = restaurantService.removeRestaurantById(100L);
            assertThat(removed).isFalse();
        }

        @Test
        void testFindAllRestaurant() {
            restaurantRepo.saveAll(List.of(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build(),
                    Restaurant.builder()
                            .name("Тортилья")
                            .description("Дом Тортильи")
                            .kitchenType(KitchenType.MEXICAN)
                            .averageCheck(500.0)
                            .userRating(BigDecimal.valueOf(4.2))
                            .build()));
            List<RestaurantResponseDTO> all = restaurantService.findAllRestaurant();

            assertThat(all).hasSize(2);
        }

        @Test
        void testRecalculateRating() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            Visitor visitor1 = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            Visitor visitor2 = visitorRepo.save(Visitor.builder()
                    .name("Анастасия")
                    .age(25)
                    .gender(Gender.FEMALE)
                    .build());

            VisitorReview review1 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor1.getId(), restaurant.getId()))
                    .visitor(visitor1)
                    .restaurant(restaurant)
                    .review("Скука")
                    .rating(4)
                    .build();
            VisitorReview review2 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor2.getId(), restaurant.getId()))
                    .visitor(visitor2)
                    .restaurant(restaurant)
                    .rating(5)
                    .build();

            visitorReviewRepo.saveAll(List.of(review1, review2));

            BigDecimal newRating = restaurantService.recalculateRating(restaurant.getId());

            assertThat(newRating).isEqualByComparingTo(BigDecimal.valueOf(4.5));

            Restaurant updated = restaurantRepo.findById(restaurant.getId()).get();

            assertThat(updated.getUserRating()).isEqualByComparingTo(BigDecimal.valueOf(4.5));
        }

        @Test
        void testRecalculateRatingNoReviews() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build());
            BigDecimal newRating = restaurantService.recalculateRating(restaurant.getId());

            assertThat(newRating).isEqualByComparingTo(BigDecimal.ZERO);

            Restaurant updated = restaurantRepo.findById(restaurant.getId()).get();

            assertThat(updated.getUserRating()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void testUpdateRating() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build());
            RestaurantResponseDTO updatedDto = restaurantService.updateRating(restaurant.getId(), BigDecimal.valueOf(4.0));

            assertThat(updatedDto.userRating()).isEqualByComparingTo(BigDecimal.valueOf(4.0));
        }

        @Test
        void testGetRestaurantById() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build());
            RestaurantResponseDTO dto = restaurantService.getRestaurantById(restaurant.getId());
            assertThat(dto.name()).isEqualTo("Монт Бланк");
        }

        @Test
        void testUpdateRestaurantById() {
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build());
            RestaurantRequestDTO updateDto = new RestaurantRequestDTO("ПиццаХаус", "Переделка в пиццу",
                    KitchenType.ITALIAN, 900.0, BigDecimal.valueOf(4.8));
            RestaurantResponseDTO updated = restaurantService.updateRestaurantById(restaurant.getId(), updateDto);

            assertThat(updated.name()).isEqualTo("ПиццаХаус");
            assertThat(updated.description()).isEqualTo("Переделка в пиццу");
        }
    }

    @Nested
    class VisitorReviewServiceTests{
        @Test
        void testSaveVisitorReviewAndRecalculateRating() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            VisitorReviewRequestDTO dto = new VisitorReviewRequestDTO(visitor.getId(), restaurant.getId(), 5, "Все супер");
            VisitorReviewResponseDTO saved = visitorReviewService.saveVisitorReview(dto);

            assertThat(saved.rating()).isEqualTo(5);

            Restaurant updatedRestaurant = restaurantRepo.findById(restaurant.getId()).get();
            assertThat(updatedRestaurant.getUserRating()).isEqualByComparingTo(BigDecimal.valueOf(5.0));
        }

        @Test
        void testRemoveVisitorReviewAndRecalculateRating() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            VisitorReviewId id = new VisitorReviewId(visitor.getId(), restaurant.getId());
            VisitorReview review = VisitorReview.builder()
                    .id(id)
                    .visitor(visitor)
                    .restaurant(restaurant)
                    .rating(5)
                    .build();
            visitorReviewRepo.save(review);

            boolean removed = visitorReviewService.removeVisitorReview(visitor.getId(), restaurant.getId());

            assertThat(removed).isTrue();

            Restaurant updatedRestaurant = restaurantRepo.findById(restaurant.getId()).get();

            assertThat(updatedRestaurant.getUserRating()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void testRemoveVisitorReviewNotFound() {
            boolean removed = visitorReviewService.removeVisitorReview(100L, 100L);
            assertThat(removed).isFalse();
        }

        @Test
        void testFindAllVisitorReview() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());

            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            VisitorReview review = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor.getId(), restaurant.getId()))
                    .visitor(visitor)
                    .restaurant(restaurant)
                    .rating(5)
                    .build();
            visitorReviewRepo.save(review);

            Pageable pageable = PageRequest.of(0, 10);
            Page<VisitorReviewResponseDTO> page = visitorReviewService.findAllVisitorReview(pageable);
            assertThat(page.getContent()).hasSize(1);
        }

        @Test
        void testFindVisitorReviewById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());

            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            VisitorReview review = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor.getId(), restaurant.getId()))
                    .visitor(visitor)
                    .restaurant(restaurant)
                    .rating(5)
                    .build();
            visitorReviewRepo.save(review);

            VisitorReviewResponseDTO dto = visitorReviewService.findVisitorReviewById(visitor.getId(), restaurant.getId());
            assertThat(dto.rating()).isEqualTo(5);
        }

        @Test
        void testFindVisitorReviewByIdNotFound() {
            assertThatThrownBy(() -> visitorReviewService.findVisitorReviewById(100L, 100L))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void testUpdateVisitorReviewById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());

            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.ZERO)
                    .build());

            VisitorReview review = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor.getId(), restaurant.getId()))
                    .visitor(visitor)
                    .restaurant(restaurant)
                    .rating(5)
                    .review("Все супер")
                    .build();
            visitorReviewRepo.save(review);

            VisitorReviewRequestDTO updateDto = new VisitorReviewRequestDTO(visitor.getId(), restaurant.getId(), 3, "Ресторан скатился");
            VisitorReviewResponseDTO updated = visitorReviewService.updateVisitorReviewById(visitor.getId(), restaurant.getId(), updateDto);

            assertThat(updated.rating()).isEqualTo(3);
            assertThat(updated.review()).isEqualTo("Ресторан скатился");
        }

        @Test
        void testFindRestaurantsByMinRating() {
            Restaurant restaurant1 = restaurantRepo.save(Restaurant.builder()
                    .name("Тортилья")
                    .description("Дом Тортильи")
                    .kitchenType(KitchenType.MEXICAN)
                    .averageCheck(500.0)
                    .userRating(BigDecimal.valueOf(3.9))
                    .build());
            Restaurant restaurant2 = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5))
                    .build());

            List<Restaurant> result = visitorReviewService.findRestaurantsByMinRating(BigDecimal.valueOf(4.0));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Монт Бланк");
        }
    }
}
