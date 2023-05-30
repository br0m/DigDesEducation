package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

}

