package com.restaurant.rating.mapper;

import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    //
    @Mapping(target = "id", ignore = true)
    Visitor toEntity(VisitorRequestDTO requestDTO);

    VisitorResponseDTO toDto(Visitor visitor);

    List<VisitorResponseDTO> toDtoList(List<Visitor> visitors);
}
