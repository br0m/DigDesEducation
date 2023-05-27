package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.services.TaskService;
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
@RequestMapping("/task")
@Tag(name = "TaskController", description = "Контроллер задач")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создание новой задачи")
    @PostMapping("/save")
    public TaskViewDto create(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @Operation(summary = "Обновление данных задачи")
    @PutMapping("/{id}")
    public TaskViewDto update(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto, id);
    }

    @Operation(summary = "Обновление статуса задачи")
    @PatchMapping("/{id}/{status}")
    public TaskViewDto setStatus(@PathVariable Integer id, @PathVariable TaskStatus status) {
        return taskService.setStatus(id, status);
    }

    @Operation(summary = "Поиск задач по наименованию, статусу, автору, исполнителю и датам")
    @PostMapping("/find")
    public List<TaskViewDto> find(@RequestBody FindTaskDto findTaskDto) {
        return taskService.find(findTaskDto);
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

