package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.request.RestaurantRequestDTO;
import com.restaurant.rating.dto.response.RestaurantResponseDTO;
import com.restaurant.rating.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "id", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO restaurantRequestDTO);

    RestaurantResponseDTO toDto(Restaurant restaurant);

    List<RestaurantResponseDTO> toDTOList(List<Restaurant> restaurants);
}
