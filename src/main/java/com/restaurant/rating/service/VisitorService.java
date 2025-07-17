package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.mapper.VisitorMapper;
import com.restaurant.rating.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public VisitorService(VisitorRepository visitorRepository, VisitorMapper visitorMapper) {
        this.visitorRepository = visitorRepository;
        this.visitorMapper = visitorMapper;
    }

    public VisitorResponseDTO saveVisitor(VisitorRequestDTO requestDTO) {
        Visitor visitor = visitorMapper.toEntity(requestDTO);
        Visitor saved = visitorRepository.save(visitor);
        return visitorMapper.toDto(saved);
    }

    public boolean removeVisitorById(Long id) {
        return visitorRepository.removeById(id);
    }

    public List<VisitorResponseDTO> findAllVisitors() {
        List<Visitor> visitors = visitorRepository.findAll();
        return visitorMapper.toDtoList(visitors);
    }

    public VisitorResponseDTO getVisitorById(Long id){
        Visitor visitor = visitorRepository.findById(id);
        return visitorMapper.toDto(visitor);
    }

    public VisitorResponseDTO updateVisitorById(Long id, VisitorRequestDTO requestDTO) {
        Visitor updatedVisitor = visitorMapper.toEntity(requestDTO);
        Visitor result = visitorRepository.updateVisitor(id, updatedVisitor);
        return visitorMapper.toDto(result);
    }
}
