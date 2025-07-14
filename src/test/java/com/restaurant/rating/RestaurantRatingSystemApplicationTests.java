package com.restaurant.rating;

import com.restaurant.rating.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantRatingSystemApplicationTests {

	@Autowired
	private VisitorRepository visitorRepository;

	@Test
	void contextLoads() {
	}
}
