package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.request.VisitorReviewRequestDTO;
import com.restaurant.rating.dto.response.VisitorReviewResponseDTO;
import com.restaurant.rating.entity.VisitorReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorReviewMapper {

    @Mappings({
            @Mapping(target = "id.visitorId", source = "visitorId"),
            @Mapping(target = "id.restaurantId", source = "restaurantId"),
            @Mapping(target = "visitor", ignore = true),
            @Mapping(target = "restaurant", ignore = true)
    })
    VisitorReview toEntity(VisitorReviewRequestDTO requestDTO);

    @Mappings({
            @Mapping(target = "visitorId", source = "id.visitorId"),
            @Mapping(target = "restaurantId", source = "id.restaurantId")
    })

    VisitorReviewResponseDTO toDto(VisitorReview visitorReview);

    List<VisitorReviewResponseDTO> toDtoList(List<VisitorReview> reviews);
}
