package com.digdes.java2023.services;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface ProjectService {

    ProjectDto getById(@NotNull Integer id);

    ProjectDto create(@Valid ProjectDto projectDto);

    ProjectDto update(@NotNull Integer id, @Valid ProjectDto projectDto);

    ProjectDto setStatus(@NotNull Integer id, @NotNull ProjectStatus status);

    List<ProjectDto> find(@Size(min = 3) String text, @NotEmpty List<ProjectStatus> statuses);
}
