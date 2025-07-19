package com.restaurant.rating.service;

import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.entity.Visitor;
import com.restaurant.rating.enums.Gender;
import com.restaurant.rating.mapper.VisitorMapper;
import com.restaurant.rating.repository.VisitorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {
    @Mock
    private VisitorRepo visitorRepo;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor;
    private VisitorRequestDTO requestDTO;
    private VisitorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new VisitorRequestDTO("Валентина", 25, Gender.FEMALE);
        visitor = new Visitor();
        visitor.setId(1L);
        visitor.setName("Валентина");
        visitor.setAge(25);
        visitor.setGender(Gender.FEMALE);

        responseDTO = new VisitorResponseDTO(1L, "Валентина", 25, Gender.FEMALE);
    }
    @Test
    void saveVisitorShouldSaveAndReturnDto() {
        when(visitorMapper.toEntity(requestDTO)).thenReturn(visitor);
        when(visitorRepo.save(visitor)).thenReturn(visitor);
        when(visitorMapper.toDto(visitor)).thenReturn(responseDTO);

        VisitorResponseDTO result = visitorService.saveVisitor(requestDTO);

        assertEquals(responseDTO, result);
        assertNotNull(result);
    }

    @Test
    void removeVisitorByIdShouldRemoveAndReturnTrueWhenExists() {
        Long id = 1L;
        when(visitorRepo.existsById(id)).thenReturn(true);

        boolean result = visitorService.removeVisitorById(id);
        assertTrue(result);
        verify(visitorRepo, times(1)).existsById(id);
    }

    @Test
    void removeVisitorByIdShouldRemoveAndReturnTrueWhenNotExists() {
        Long id = 1L;
        when(visitorRepo.existsById(id)).thenReturn(false);

        boolean result = visitorService.removeVisitorById(id);
        assertFalse(result);

    }

    @Test
    void findAllVisitorsShouldReturnListOfDto() {
        List<Visitor> visitorList = Collections.singletonList(visitor);
        List<VisitorResponseDTO> visitorResponseDTOList = Collections.singletonList(responseDTO);
        when(visitorRepo.findAll()).thenReturn(visitorList);
        when(visitorMapper.toDtoList(visitorList)).thenReturn(visitorResponseDTOList);

        List<VisitorResponseDTO> result = visitorService.findAllVisitors();

        assertNotNull(result);
        assertEquals(visitorResponseDTOList, result);
        assertEquals(1, result.size());
        assertEquals(visitorResponseDTOList.size(), result.size());
    }

    @Test
    void findAllVisitorsShouldReturnEmptyListOfDtoWhenNoVisitors() {
        when(visitorRepo.findAll()).thenReturn(Collections.emptyList());
        when(visitorMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<VisitorResponseDTO> result = visitorService.findAllVisitors();

        assertEquals(Collections.emptyList(), result);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getVisitorByIdShouldReturnDtoWhenExists() {
        Long id = 1L;
        when(visitorRepo.findById(id)).thenReturn(Optional.of(visitor));
        when(visitorMapper.toDto(visitor)).thenReturn(responseDTO);

        VisitorResponseDTO result = visitorService.getVisitorById(id);

        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void getVisitorByIdShouldReturnDtoWhenNotExists() {
        Long id = 1L;
        when(visitorRepo.findById(id)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorService.getVisitorById(id));
        assertEquals("Посетитель с id: " + id + " не найден", exception.getMessage());
    }

    @Test
    void updateVisitorByIdShouldUpdateAndReturnDtoWhenExists() {
        Long id = 1L;
        Visitor updatedVisitor = new Visitor();
        updatedVisitor.setId(id);
        updatedVisitor.setName(requestDTO.name());
        updatedVisitor.setAge(requestDTO.age());
        updatedVisitor.setGender(requestDTO.gender());

        VisitorResponseDTO updatedDto = new VisitorResponseDTO(id, requestDTO.name(), requestDTO.age(), requestDTO.gender());

        when(visitorRepo.findById(id)).thenReturn(Optional.of(visitor));
        when(visitorRepo.save(visitor)).thenReturn(updatedVisitor);
        when(visitorMapper.toDto(updatedVisitor)).thenReturn(updatedDto);

        VisitorResponseDTO result = visitorService.updateVisitorById(id, requestDTO);

        assertNotNull(result);
        assertEquals(updatedDto, result);
        assertEquals(requestDTO.name(), visitor.getName());
        assertEquals(requestDTO.age(), visitor.getAge());
        assertEquals(requestDTO.gender(), visitor.getGender());
    }

    @Test
    void updateVisitorByIdShouldUpdateAndReturnDtoWhenNotExists() {
        Long id = 1L;
        when(visitorRepo.findById(id)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> visitorService.updateVisitorById(id, requestDTO));
        assertEquals("Посетитель с id: " + id + " не найден", exception.getMessage());
    }
}