package com.restaurant.rating.integrationtest;

import com.restaurant.rating.entity.Restaurant;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.entity.VisitorReviewId;
import com.restaurant.rating.enums.Gender;
import com.restaurant.rating.enums.KitchenType;
import com.restaurant.rating.repository.RestaurantRepo;
import com.restaurant.rating.repository.VisitorRepo;
import com.restaurant.rating.repository.VisitorReviewRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryIntegrationTests {
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

    @BeforeEach
    void setUp() {
        visitorReviewRepo.deleteAll();
        visitorRepo.deleteAll();
        restaurantRepo.deleteAll();
    }

    @Nested
    class VisitorRepoTests {

        @Test
        void testSaveAndFindById() {
            Visitor visitor = Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();
            Visitor saved = visitorRepo.save(visitor);

            assertThat(saved.getId()).isNotNull();

            Visitor found = visitorRepo.findById(saved.getId()).orElse(null);

            assertThat(found).isNotNull();
            assertThat(found.getName()).isEqualTo("Александр");
        }

        @Test
        void testFindAll() {
            Visitor visitor1 = Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();
            Visitor visitor2 = Visitor.builder()
                    .name("Анастасия")
                    .age(25)
                    .gender(Gender.FEMALE)
                    .build();
            visitorRepo.saveAll(List.of(visitor1, visitor2));

            List<Visitor> all = visitorRepo.findAll();

            assertThat(all).hasSize(2);
        }

        @Test
        void testDeleteById() {
            Visitor visitor = Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build();
            Visitor saved = visitorRepo.save(visitor);

            visitorRepo.deleteById(saved.getId());

            assertThat(visitorRepo.findById(saved.getId())).isEmpty();
        }
    }

    @Nested
    class RestaurantRepoTests {

        @Test
        void testSaveAndFindById() {
            Restaurant restaurant = Restaurant.builder()
                    .name("Пицца")
                    .description("Итальянская пицца")
                    .kitchenType(KitchenType.ITALIAN)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(4.0))
                    .build();
            Restaurant saved = restaurantRepo.save(restaurant);

            assertThat(saved.getId()).isNotNull();

            Restaurant found = restaurantRepo.findById(saved.getId()).orElse(null);

            assertThat(found).isNotNull();
            assertThat(found.getName()).isEqualTo("Пицца");
        }

        @Test
        void testFindAll() {
            Restaurant restaurant1 = Restaurant.builder()
                    .name("Пицца")
                    .description("Итальянская пицца")
                    .kitchenType(KitchenType.ITALIAN)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(4.0))
                    .build();
            Restaurant restaurant2 = Restaurant.builder()
                    .name("Бургеры")
                    .description("БургерХаус")
                    .kitchenType(KitchenType.AMERICAN)
                    .averageCheck(700.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build();
            restaurantRepo.saveAll(List.of(restaurant1, restaurant2));

            List<Restaurant> all = restaurantRepo.findAll();

            assertThat(all).hasSize(2);
        }

        @Test
        void testDeleteById() {
            Restaurant restaurant = Restaurant.builder()
                    .name("ПиццаХаус")
                    .description("Итальянская пицца")
                    .kitchenType(KitchenType.ITALIAN)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(4.5))
                    .build();
            Restaurant saved = restaurantRepo.save(restaurant);

            restaurantRepo.deleteById(saved.getId());

            assertThat(restaurantRepo.findById(saved.getId())).isEmpty();
        }

        @Test
        void testFindByUserRatingGreaterThanEqual() {
            Restaurant restaurant1 = Restaurant.builder()
                    .name("Суши")
                    .description("СушиБар")
                    .kitchenType(KitchenType.JAPANESE)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(3.0))
                    .build();
            Restaurant restaurant2 = Restaurant.builder()
                    .name("Тортилья")
                    .description("Дом Тортильи")
                    .kitchenType(KitchenType.MEXICAN)
                    .averageCheck(500.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build();
            Restaurant restaurant3 = Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build();
            restaurantRepo.saveAll(List.of(restaurant1, restaurant2, restaurant3));

            List<Restaurant> result = restaurantRepo.findByUserRatingGreaterThanEqual(BigDecimal.valueOf(4.0));

            assertThat(result).hasSize(2);
            assertThat(result).extracting(Restaurant::getName).containsExactlyInAnyOrder("Тортилья", "Монт Бланк");
        }

        @Test
        void testFindRestaurantsWithRatingGreaterThanEqual() {
            Restaurant restaurant1 = Restaurant.builder()
                    .name("Суши")
                    .description("СушиБар")
                    .kitchenType(KitchenType.JAPANESE)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(3.0))
                    .build();
            Restaurant restaurant2 = Restaurant.builder()
                    .name("Тортилья")
                    .description("Дом Тортильи")
                    .kitchenType(KitchenType.MEXICAN)
                    .averageCheck(500.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build();
            Restaurant restaurant3 = Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build();
            restaurantRepo.saveAll(List.of(restaurant1, restaurant2, restaurant3));

            List<Restaurant> result = restaurantRepo.findRestaurantsWithRatingGreaterThanEqual(BigDecimal.valueOf(4.0));

            assertThat(result).hasSize(2);
            assertThat(result).extracting(Restaurant::getName).containsExactlyInAnyOrder("Тортилья", "Монт Бланк");
        }
    }

    @Nested
    class VisitorReviewRepoTests{

        @Test
        void testSaveAndFindById() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("ПиццаХаус")
                    .description("Итальянская пицца")
                    .kitchenType(KitchenType.ITALIAN)
                    .averageCheck(900.0)
                    .userRating(BigDecimal.valueOf(4.5))
                    .build());

            VisitorReviewId id = new VisitorReviewId(visitor.getId(), restaurant.getId());
            VisitorReview review = VisitorReview.builder()
                    .id(id)
                    .visitor(visitor)
                    .restaurant(restaurant)
                    .rating(5)
                    .review("Все шикарно")
                    .build();
            VisitorReview saved = visitorReviewRepo.save(review);
            VisitorReview found = visitorReviewRepo.findById(saved.getId()).orElse(null);

            assertThat(found).isNotNull();
            assertThat(found.getRating()).isEqualTo(5);
        }

        @Test
        void testFindAll() {
            Visitor visitor = visitorRepo.save(Visitor.builder()
                    .name("Александр")
                    .age(20)
                    .gender(Gender.MALE)
                    .build());
            Restaurant restaurant1 = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build());
            Restaurant restaurant2 = restaurantRepo.save(Restaurant.builder()
                    .name("Тортилья")
                    .description("Дом Тортильи")
                    .kitchenType(KitchenType.MEXICAN)
                    .averageCheck(500.0)
                    .userRating(BigDecimal.valueOf(4.2))
                    .build());

            VisitorReview review1 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor.getId(), restaurant1.getId()))
                    .visitor(visitor)
                    .restaurant(restaurant1)
                    .review("Скучный ресторан")
                    .rating(4)
                    .build();
            VisitorReview review2 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor.getId(), restaurant2.getId()))
                    .visitor(visitor)
                    .restaurant(restaurant2)
                    .rating(5)
                    .build();
            visitorReviewRepo.saveAll(List.of(review1, review2));

            List<VisitorReview> all = visitorReviewRepo.findAll();

            assertThat(all).hasSize(2);
        }

        @Test
        void testDeleteById() {
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
                    .userRating(BigDecimal.valueOf(5.0))
                    .build());
            VisitorReviewId id = new VisitorReviewId(visitor.getId(), restaurant.getId());
            VisitorReview review = VisitorReview.builder().id(id).visitor(visitor).restaurant(restaurant).rating(4).build();
            visitorReviewRepo.save(review);

            visitorReviewRepo.deleteById(id);

            assertThat(visitorReviewRepo.findById(id)).isEmpty();
        }

        @Test
        void testFindByRestaurantId() {
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

            Restaurant restaurant = restaurantRepo.save(Restaurant.builder()
                    .name("Монт Бланк")
                    .description("Европейская еда")
                    .kitchenType(KitchenType.EUROPEAN)
                    .averageCheck(1200.0)
                    .userRating(BigDecimal.valueOf(5.0))
                    .build());

            VisitorReview review1 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor1.getId(), restaurant.getId()))
                    .visitor(visitor1)
                    .restaurant(restaurant)
                    .rating(4)
                    .build();
            VisitorReview review2 = VisitorReview.builder()
                    .id(new VisitorReviewId(visitor2.getId(), restaurant.getId()))
                    .visitor(visitor2)
                    .restaurant(restaurant)
                    .rating(5)
                    .build();

            visitorReviewRepo.saveAll(List.of(review1, review2));

            List<VisitorReview> result = visitorReviewRepo.findByRestaurantId(restaurant.getId());

            assertThat(result).hasSize(2);
        }
    }
}
