package com.restaurant.rating.service;

import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor saveVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public boolean removeVisitor(Visitor visitor) {
        return visitorRepository.remove(visitor);
    }

    public List<Visitor> findAllVisitors(){
        return visitorRepository.findAll();
    }

}
