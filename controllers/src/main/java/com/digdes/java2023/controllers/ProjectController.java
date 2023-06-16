package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/project")
@Tag(name = "ProjectController", description = "Контроллер проектов")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Создание нового проекта")
    @PostMapping("/save")
    public ProjectDto create(@RequestBody ProjectDto projectDto) {
        log.info("New create project request received");
        return projectService.create(projectDto);
    }

    @Operation(summary = "Обновление данных проекта")
    @PutMapping("/{id}")
    public ProjectDto update(@PathVariable Integer id, @RequestBody ProjectDto projectDto) {
        log.info("New update project request received");
        return projectService.update(id, projectDto);
    }

    @Operation(summary = "Поиск проектов по наименованию, коду и списку статусов")
    @GetMapping("/find")
    public List<ProjectDto> find(@RequestParam String text, @RequestParam List<ProjectStatus> statuses) {
        log.info("New search project request received");
        return projectService.find(text, statuses);
    }

    @Operation(summary = "Изменение статуса проекта")
    @PatchMapping("/{id}/{status}")
    public ProjectDto setStatus(@PathVariable Integer id, @PathVariable ProjectStatus status) {
        log.info("New set project status request received");
        return projectService.setStatus(id, status);
    }

}
