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

    public boolean removeById(Long id) {
        Visitor visitorToRemove = findById(id);
        if (visitorToRemove != null) {
            return visitors.remove(visitorToRemove);
        }
        return false;
    }

    public List<Visitor> findAll(){
        return new ArrayList<>(visitors);
    }

    public Visitor findById(Long id){
        return visitors.stream()
                .filter(visitor -> visitor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Visitor updateVisitor(Long id, Visitor updatedVisitor) {
        Visitor oldVisitor = findById(id);
        if (oldVisitor == null) {
            throw new IllegalArgumentException("Посетитель с id " + id + " не найден");
        }
        oldVisitor.setName(updatedVisitor.getName());
        oldVisitor.setAge(updatedVisitor.getAge());
        oldVisitor.setGender(updatedVisitor.getGender());
        return oldVisitor;
    }
}
