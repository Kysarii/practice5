package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepo extends JpaRepository<Visitor, Long> {

}
