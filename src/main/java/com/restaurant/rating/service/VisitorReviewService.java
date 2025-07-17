package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.VisitorReview;
import com.restaurant.rating.mapper.VisitorReviewMapper;
import com.restaurant.rating.repository.VisitorReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorReviewService {
    private final VisitorReviewRepository visitorReviewRepository;
    private final RestaurantService restaurantService;
    private final VisitorReviewMapper visitorReviewMapper;

    public VisitorReviewService(VisitorReviewRepository visitorReviewRepository, RestaurantService restaurantService, VisitorReviewMapper visitorReviewMapper) {
        this.visitorReviewRepository = visitorReviewRepository;
        this.restaurantService = restaurantService;
        this.visitorReviewMapper = visitorReviewMapper;
    }

    public VisitorReviewResponseDTO saveVisitorReview(VisitorReviewRequestDTO requestDTO) {
        VisitorReview visitorReview =  visitorReviewMapper.toEntity(requestDTO);
        VisitorReview savedVisitorReview = visitorReviewRepository.save(visitorReview);
        restaurantService.recalculateRating(savedVisitorReview.getRestaurantId());
        return visitorReviewMapper.toDto(savedVisitorReview);
    }

    public boolean removeVisitorReview(Long id) {
        return visitorReviewRepository.removeById(id);
    }

    public List<VisitorReviewResponseDTO> findAllVisitorReview(){
        List<VisitorReview> visitorReviews = visitorReviewRepository.findAll();
        return  visitorReviewMapper.toDtoList(visitorReviews);
    }

    public VisitorReviewResponseDTO findVisitorReviewById(Long id) {
        VisitorReview visitorReview = visitorReviewRepository.findById(id);
        return visitorReviewMapper.toDto(visitorReview);
    }

    public VisitorReviewResponseDTO updateVisitorReviewById(Long id, VisitorReviewRequestDTO requestDTO) {
        VisitorReview updatedVisitorReview = visitorReviewMapper.toEntity(requestDTO);
        VisitorReview result = visitorReviewRepository.updateVisitorReviewById(id, updatedVisitorReview);
        return visitorReviewMapper.toDto(result);
    }

}
