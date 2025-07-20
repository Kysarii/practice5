package com.restaurant.rating.controller;

import com.restaurant.rating.dto.request.VisitorRequestDTO;
import com.restaurant.rating.dto.response.VisitorResponseDTO;
import com.restaurant.rating.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление посетителями", description = "API для управления посетителями")
@Validated
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    @Operation(summary = "Получить всех посетителей", description = "Возвращает список всех посетителей")
    public List<VisitorResponseDTO> findAllVisitors() {
        return visitorService.findAllVisitors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить посетителя по ID", description = "Возвращает информацию о посетителе по его ID")
    public VisitorResponseDTO findVisitorById(@PathVariable Long  id) {
        return visitorService.getVisitorById(id);
    }

    @PostMapping
    @Operation(summary = "Создать нового посетителя", description = "Создает нового посетителя с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorResponseDTO createVisitor(@Valid @RequestBody VisitorRequestDTO requestVisitorDTO) {
        return visitorService.saveVisitor(requestVisitorDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные посетителя", description = "Обновляет информацию о посетителе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
    })
    public VisitorResponseDTO updateVisitor(@PathVariable Long id, @Valid @RequestBody VisitorRequestDTO requestVisitorDTO) {
        return visitorService.updateVisitorById(id, requestVisitorDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя", description = "Удаляет посетителя по его ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVisitor(@PathVariable Long id) {
        boolean deleted = visitorService.removeVisitorById(id);
        if (!deleted) {
            throw new NoSuchElementException("Посетитель с id: " + id + " не найден");
        }
    }

}
