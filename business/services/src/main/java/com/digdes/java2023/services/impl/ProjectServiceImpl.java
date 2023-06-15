package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.mapping.ProjectMapper;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.services.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Validated
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final ProjectMapper mapper;

    @Override
    public ProjectDto getById(@NotNull Integer id) {
        return mapper.toDto(getEntityById(id));
    }

    @Override
    public ProjectDto create(@Valid ProjectDto projectDto) {
        log.info("Creating new project");
        log.debug(projectDto.toString());

        Project project = mapper.toEntity(projectDto);
        checkCodename(project.getCodename());
        project.setStatus(ProjectStatus.DRAFT);
        project.setId(null);
        projectRepositoryJpa.save(project);

        log.info("New project successfully created");
        log.debug(project.toString());
        return mapper.toDto(project);
    }

    @Override
    public ProjectDto update(@NotNull Integer id, @Valid ProjectDto projectDto) {
        log.info("Updating project with id = " + id);
        log.debug(projectDto!=null ? projectDto.toString() : null);

        Project oldProject = getEntityById(id);
        Project project = mapper.toEntity(projectDto);
        if(!project.getCodename().equals(oldProject.getCodename()))
            checkCodename(project.getCodename());
        project.setId(id);
        int count = projectRepositoryJpa.update(project.getCodename(), project.getTitle(), project.getDescription(), id);

        log.info("Project with id = " + id + " successfully updated");
        log.debug(project.toString());
        return dtoOrNull(count, project);
    }

    @Override
    public ProjectDto setStatus(@NotNull Integer id, @NotNull ProjectStatus status) {
        log.info("Setting new status = " + status + " for project with id = " + id);

        Project project = getEntityById(id);
        if (status.ordinal() - project.getStatus().ordinal() != 1) {
            log.warn("New status is not according workflow");
            throw new PropertyValueException("Not according workflow", "project", "status");
        }

        int count = projectRepositoryJpa.setStatus(id, status);
        project.setStatus(status);

        log.info("Status successfully modified");
        log.debug(project.toString());
        return dtoOrNull(count, project);
    }

    @Override
    public List<ProjectDto> find(@Size(min = 3) String text, @NotEmpty List<ProjectStatus> statuses) {
        log.info("Searching project with text = " + text + " and statuses = " + statuses.toString());
        List<Project> projects = projectRepositoryJpa.findAllByTextAndStatuses(text, statuses);
        log.info("Search completed");
        log.debug(projects.toString());
        return mapper.toListDto(projects);
    }

    private Project getEntityById(@NotNull Integer id) {
        log.info("Searching project with id = " + id);

        Project project = projectRepositoryJpa.findById(id).orElse(null);
        if(project==null) {
            log.warn("Project with id = " + id + " not found");
            throw new ObjectNotFoundException(id, "project");
        }

        log.info("Project with id = " + id + " successfully found");
        log.debug(project.toString());
        return project;
    }

    private ProjectDto dtoOrNull(int count, Project project) {
        return count == 1 ? mapper.toDto(project) : null;
    }

    private void checkCodename(String codename) {
        List<Project> projects = projectRepositoryJpa.findAll();
        projects.stream().filter(p -> p.getCodename().equals(codename)).findAny().ifPresent(p -> {
            log.warn("Project codename = " + p.getCodename() + " is not unique");
            throw new PropertyValueException("Value should be unique", "project", "codename");
        });
    }
}
