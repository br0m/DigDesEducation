package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

}
