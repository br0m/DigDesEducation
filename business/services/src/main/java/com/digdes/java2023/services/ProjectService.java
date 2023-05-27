package com.digdes.java2023.services;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ProjectService {

    ProjectDto getById(@NotNull Integer id);

    ProjectDto create(@Valid ProjectDto projectDto);

    ProjectDto update(@NotNull Integer id, @Valid ProjectDto projectDto);

    ProjectDto setStatus(@NotNull Integer id, ProjectStatus status);

    List<ProjectDto> find(@Min(3) String text, @NotEmpty List<ProjectStatus> statuses);
}
