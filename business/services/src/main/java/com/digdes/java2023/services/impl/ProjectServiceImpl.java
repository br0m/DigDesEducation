package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.mapping.ProjectMapper;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.services.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final ProjectMapper mapper;
    private Project project;


    @Override
    public ProjectDto getById(@NotNull Integer id) {
        return mapper.toDto(getEntityById(id));
    }

    @Override
    public ProjectDto create(@Valid ProjectDto projectDto) {
        project = mapper.toEntity(projectDto);
        checkCodename(project.getCodename());
        project.setStatus(ProjectStatus.DRAFT);
        projectRepositoryJpa.save(project);
        return mapper.toDto(project);
    }

    @Override
    public ProjectDto update(@NotNull Integer id, @Valid ProjectDto projectDto) {
        project = mapper.toEntity(projectDto);
        checkCodename(project.getCodename());
        project.setId(id);
        int count = projectRepositoryJpa.update(project.getCodename(), project.getTitle(), project.getDescription(), id);
        return dtoOrNull(count);
    }

    @Override
    public ProjectDto setStatus(@NotNull Integer id, @NotNull ProjectStatus status) {
        project = getEntityById(id);
        if (status.ordinal() - project.getStatus().ordinal() != 1)
            throw new PropertyValueException("Not according workflow", "project", "status");
        int count = projectRepositoryJpa.setStatus(id, status);
        project.setStatus(status);
        return dtoOrNull(count);
    }

    @Override
    public List<ProjectDto> find(@Min(3) String text, @NotEmpty List<ProjectStatus> statuses) {
        List<Project> projects = projectRepositoryJpa.findAllByTextAndStatuses(text, statuses);
        return mapper.toListDto(projects);
    }

    private Project getEntityById(@NotNull Integer id) {
        return projectRepositoryJpa.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "project"));
    }

    private ProjectDto dtoOrNull(int count) {
        return count == 1 ? mapper.toDto(project) : null;
    }

    private void checkCodename(String codename) {
        List<Project> projects = projectRepositoryJpa.findAll();
        projects.stream().filter(p -> p.getCodename().equals(codename)).findAny().ifPresent(p -> {
            throw new PropertyValueException("Value should be unique", "project", "codename");
        });
    }
}