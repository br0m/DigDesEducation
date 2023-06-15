package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/task")
@Tag(name = "TaskController", description = "Контроллер задач")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создание новой задачи")
    @PostMapping("/save")
    public TaskViewDto create(@RequestBody TaskDto taskDto) {
        log.info("New create task request received");
        return taskService.create(taskDto);
    }

    @Operation(summary = "Обновление данных задачи")
    @PutMapping("/{id}")
    public TaskViewDto update(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        log.info("New update task request received");
        return taskService.update(taskDto, id);
    }

    @Operation(summary = "Обновление статуса задачи")
    @PatchMapping("/{id}/{status}")
    public TaskViewDto setStatus(@PathVariable Integer id, @PathVariable TaskStatus status) {
        log.info("New task set status request received");
        return taskService.setStatus(id, status);
    }

    @Operation(summary = "Поиск задач по наименованию, статусу, автору, исполнителю и датам")
    @PostMapping("/find")
    public List<TaskViewDto> find(@RequestBody FindTaskDto findTaskDto) {
        log.info("New search task request received");
        return taskService.find(findTaskDto);
    }

}

