package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Tag(name = "ProjectController", description = "Контроллер проектов")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Создание нового проекта")
    @PostMapping("/save")
    public ProjectDto create(@RequestBody ProjectDto projectDto) {
        return projectService.create(projectDto);
    }

    @Operation(summary = "Обновление данных проекта")
    @PutMapping("/{id}")
    public ProjectDto update(@PathVariable Integer id, @RequestBody ProjectDto projectDto) {
        return projectService.update(id, projectDto);
    }

    @Operation(summary = "Поиск проектов по наименованию, коду и списку статусов")
    @GetMapping("/find")
    public List<ProjectDto> find(@RequestParam String text, @RequestParam List<ProjectStatus> statuses) {
        return projectService.find(text, statuses);
    }

    @Operation(summary = "Изменение статуса проекта")
    @PatchMapping("/{id}/{status}")
    public ProjectDto setStatus(@PathVariable Integer id, @PathVariable ProjectStatus status) {
        return projectService.setStatus(id, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyValueException.class)
    public ErrorMessage handleException(PropertyValueException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorMessage handleException(ObjectNotFoundException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handleException(HttpMessageNotReadableException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleException(ConstraintViolationException exception) {
        return new ErrorMessage(exception.getMessage());
    }

}
