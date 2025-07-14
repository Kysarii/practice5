package com.restaurant.rating;

import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.enums.Gender;
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

	//@Test
	//void createVisitor() {
	//	Visitor visitor = Visitor.builder()
	//			.id(null)
	//			.name("visitor")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
//
	//	Visitor visitor2 = Visitor.builder()
	//			.id(null)
	//			.name("visitor")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
//
	//	visitorRepository.save(visitor);
	//	visitorRepository.save(visitor2);
//
	//	System.out.println('e');
	//}
//
	//@Test
	//void removeVisitor() {
	//	Visitor visitor = Visitor.builder()
	//			.id(null)
	//			.name("A")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
//
	//	Visitor visitor2 = Visitor.builder()
	//			.id(null)
	//			.name("B")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
	//	visitorRepository.save(visitor);
	//	visitorRepository.save(visitor2);
	//	visitorRepository.remove(visitor2);
	//	System.out.println('e');
	//}
//
	//@Test
	//void findAllVisitors() {
	//	Visitor visitor = Visitor.builder()
	//			.id(null)
	//			.name("A")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
//
	//	Visitor visitor2 = Visitor.builder()
	//			.id(null)
	//			.name("B")
	//			.age(18)
	//			.gender(Gender.MALE)
	//			.build();
	//	visitorRepository.save(visitor);
	//	visitorRepository.save(visitor2);
	//	visitorRepository.findAll();
	//	System.out.println('e');
	//}

}
