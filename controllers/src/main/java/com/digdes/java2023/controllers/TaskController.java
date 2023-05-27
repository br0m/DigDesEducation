package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.services.TaskService;
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
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/save")
    public TaskViewDto create(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("/{id}")
    public TaskViewDto update(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto, id);
    }

    @PatchMapping("/{id}/{status}")
    public TaskViewDto setStatus(@PathVariable Integer id, @PathVariable TaskStatus status) {
        return taskService.setStatus(id, status);
    }

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

