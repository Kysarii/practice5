package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.mapper.VisitorMapper;
import com.restaurant.rating.repository.VisitorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VisitorService {

    private final VisitorRepo visitorRepo;
    private final VisitorMapper visitorMapper;

    public VisitorService(VisitorRepo visitorRepo, VisitorMapper visitorMapper) {
        this.visitorRepo = visitorRepo;
        this.visitorMapper = visitorMapper;
    }


    public VisitorResponseDTO saveVisitor(VisitorRequestDTO requestDTO) {
        Visitor visitor = visitorMapper.toEntity(requestDTO);
        Visitor saved = visitorRepo.save(visitor);
        return visitorMapper.toDto(saved);
    }

    public boolean removeVisitorById(Long id) {
        if (visitorRepo.existsById(id)){
            visitorRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<VisitorResponseDTO> findAllVisitors() {
        List<Visitor> visitors = visitorRepo.findAll();
        return visitorMapper.toDtoList(visitors);
    }

    public VisitorResponseDTO getVisitorById(Long id){
        Visitor visitor = visitorRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Посетитель с id: " + id + " не найден"));
        return visitorMapper.toDto(visitor);
    }

    public VisitorResponseDTO updateVisitorById(Long id, VisitorRequestDTO requestDTO) {
        Visitor exist = visitorRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Посетитель с id: " + id + " не найден"));
        exist.setName(requestDTO.name());
        exist.setAge(requestDTO.age());
        exist.setGender(requestDTO.gender());
        Visitor updated = visitorRepo.save(exist);
        return visitorMapper.toDto(updated);
    }
}
