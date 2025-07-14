package com.restaurant.rating.repository;

import com.restaurant.rating.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors =  new ArrayList<>();

    public Visitor save(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("Посетитель не может быть null");
        }

        if (visitors.contains(visitor)) {
            throw new IllegalStateException("Такой посетитель уже существует");
        }

        if (visitors.isEmpty()) {
            visitor.setId(0L);
        } else {
            visitor.setId(visitors.getLast().getId() + 1);
        }

        visitors.add(visitor);
        return visitor;
    }

    public boolean remove(Visitor visitor) {
        if (visitor == null) {
            return  false;
        }
        return visitors.remove(visitor);
    }

    public List<Visitor> findAll(){
        return new ArrayList<>(visitors);
    }

}
