package com.restaurant.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.enums.Gender;
import com.restaurant.rating.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitorService visitorService;

    @Autowired
    private ObjectMapper objectMapper;

    private VisitorRequestDTO requestDTO;
    private VisitorResponseDTO responseDTO;
    private final Long visitorId = 1L;

    @BeforeEach
    void setUp() {
        requestDTO = new VisitorRequestDTO("Александр", 17, Gender.MALE);
        responseDTO = new VisitorResponseDTO(visitorId, "Александр", 17, Gender.MALE);
    }

    @Test
    void findAllVisitorsShouldReturnListOfVisitors() throws Exception{
        List<VisitorResponseDTO> visitors = Collections.singletonList(responseDTO);
        when(visitorService.findAllVisitors()).thenReturn(visitors);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Александр"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(17))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(Gender.MALE.toString()));
    }

    @Test
    void findAllVisitorsShouldReturnEmptyListWhenNoVisitors() throws Exception{
        when(visitorService.findAllVisitors()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void findVisitorByIdShouldReturnVisitorWhenExists() throws Exception{
        when(visitorService.getVisitorById(visitorId)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Александр"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(17))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(Gender.MALE.toString()));
    }

    @Test
    void findVisitorByIdShouldReturnNotFoundWhenNotExists() throws Exception{
        when(visitorService.getVisitorById(visitorId))
                .thenThrow(new NoSuchElementException("Посетитель с id: " + visitorId + " не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createVisitorShouldReturnCreatedVisitorWhenValidRequest() throws Exception{
        when(visitorService.saveVisitor(any(VisitorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Александр"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(17))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(Gender.MALE.toString()));
    }

    @Test
    void createVisitorShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        VisitorRequestDTO invalidDTO = new VisitorRequestDTO("", -1, Gender.MALE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVisitorShouldReturnUpdatedVisitorWhenValidRequest() throws Exception{
        when(visitorService.updateVisitorById(eq(visitorId), any(VisitorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(visitorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Александр"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(17))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(Gender.MALE.toString()));
    }

    @Test
    void updateVisitorShouldReturnNotFoundWhenVisitorNotExists() throws Exception{
        when(visitorService.updateVisitorById(eq(visitorId), any(VisitorRequestDTO.class)))
                .thenThrow(new NoSuchElementException("Посетитель с id: " + visitorId + " не найден"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
        //
    }

    @Test
    void updateVisitorShouldReturnBadRequestWhenInvalidRequest() throws Exception{
        VisitorRequestDTO invalidDTO = new VisitorRequestDTO("", -1, Gender.MALE);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteVisitorShouldReturnNoContentWhenSuccessful() throws Exception{
        when(visitorService.removeVisitorById(visitorId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //
    }

    @Test
    void deleteVisitorShouldReturnNotFoundWhenVisitorNotExists() throws Exception{
        when(visitorService.removeVisitorById(visitorId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //
    }
}