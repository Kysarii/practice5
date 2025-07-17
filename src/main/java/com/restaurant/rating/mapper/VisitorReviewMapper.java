package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.VisitorReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorReviewMapper {

    @Mapping(target = "id", ignore = true)
    VisitorReview toEntity(VisitorReviewRequestDTO requestDTO);

    VisitorReviewResponseDTO toDto(VisitorReview visitorReview);

    List<VisitorReviewResponseDTO> toDtoList(List<VisitorReview> reviews);
}
