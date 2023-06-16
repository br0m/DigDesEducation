package com.digdes.java2023.services;

import com.digdes.java2023.ProjectOperations;
import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.mapping.ProjectMapper;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.services.impl.ProjectServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest extends ProjectOperations {

    @Spy
    ProjectRepositoryJpa projectRepositoryJpa;
    @Mock
    ProjectMapper mapper;
    @InjectMocks
    ProjectServiceImpl projectService;

    @Test
    public void createNotExistTest() throws JsonProcessingException {
        Project inputProject = genDevelopingProject();
        Project outputProject = copyProject(inputProject);
        outputProject.setId(null);
        outputProject.setStatus(ProjectStatus.DRAFT);

        ProjectDto inputProjectDto = mapToDto(inputProject);
        ProjectDto outputProjectDto = mapToDto(outputProject);

        when(mapper.toEntity(inputProjectDto)).thenReturn(inputProject);
        when(projectRepositoryJpa.findAll()).thenReturn(List.of());
        when(mapper.toDto(inputProject)).thenReturn(outputProjectDto);

        ProjectDto actualOutputProjectDto = projectService.create(inputProjectDto);
        verify(projectRepositoryJpa).save(argThat(p -> p.hashCode() == outputProject.hashCode()));
        Assertions.assertEquals(outputProjectDto.hashCode(), actualOutputProjectDto.hashCode());
    }

    @Test
    public void createExistTest() throws JsonProcessingException {
        Project inputProject = genProject();
        ProjectDto inputProjectDto = mapToDto(inputProject);

        when(mapper.toEntity(inputProjectDto)).thenReturn(inputProject);
        when(projectRepositoryJpa.findAll()).thenReturn(List.of(inputProject));

        verify(projectRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> projectService.create(inputProjectDto));
    }

    @Test
    public void getByIdExistTest() throws JsonProcessingException {
        Project project = genProject();
        ProjectDto projectDto = mapToDto(project);

        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.of(project));
        when(mapper.toDto(project)).thenReturn(projectDto);

        ProjectDto actualProjectDto = projectService.getById(project.getId());
        Assertions.assertEquals(projectDto.hashCode(), actualProjectDto.hashCode());
    }

    @Test
    public void getByIdNotExistTest() {
        Integer id = genRandomId();
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> projectService.getById(id));
    }

    @Test
    public void updateExistTest() throws JsonProcessingException {
        Project inputProject = genProject();
        Project outputProject = copyProject(inputProject);
        Integer id = inputProject.getId();

        ProjectDto inputProjectDto = mapToDto(inputProject);
        ProjectDto outputProjectDto = mapToDto(outputProject);

        when(mapper.toEntity(inputProjectDto)).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.of(genProject()));
        when(projectRepositoryJpa.update(inputProject.getCodename(), inputProject.getTitle(), inputProject.getDescription(), id)).thenReturn(1);
        when(mapper.toDto(inputProject)).thenReturn(outputProjectDto);

        ProjectDto actualOutputProjectDto = projectService.update(id, inputProjectDto);
        verify(projectRepositoryJpa).update(eq(inputProject.getCodename()), eq(inputProject.getTitle()), eq(inputProject.getDescription()), eq(id));
        Assertions.assertEquals(outputProjectDto.hashCode(), actualOutputProjectDto.hashCode());
    }

    @Test
    public void updateExistCodenameNotUniqueTest() {
        Project inputProject = genProject();
        Integer id = inputProject.getId();

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.of(genProject()));
        when(projectRepositoryJpa.findAll()).thenReturn(List.of(inputProject));

        verify(projectRepositoryJpa, times(0)).update(any(), any(), any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> projectService.update(id, any()));
    }

    @Test
    public void updateNotExistTest() {
        Integer id = genRandomId();
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.empty());
        verify(projectRepositoryJpa, times(0)).update(any(), any(), any(), any());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> projectService.update(id, any()));
    }

    @Test
    public void setCorrectStatusExistTest() throws JsonProcessingException {
        Project inputProject = genDraftProject();
        Project outputProject = copyProject(inputProject);
        Integer id = inputProject.getId();
        outputProject.setStatus(ProjectStatus.DEVELOPING);

        ProjectDto outputProjectDto = mapToDto(outputProject);

        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.of(inputProject));
        when(projectRepositoryJpa.setStatus(id, outputProject.getStatus())).thenReturn(1);
        when(mapper.toDto(inputProject)).thenReturn(outputProjectDto);

        ProjectDto actualOutputProjectDto = projectService.setStatus(id, outputProject.getStatus());

        Assertions.assertEquals(outputProjectDto.hashCode(), actualOutputProjectDto.hashCode());
    }

    @Test
    public void setIncorrectUpStatusExistTest() {
        Project inputProject = genDraftProject();
        Integer id = inputProject.getId();
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.of(inputProject));
        Assertions.assertThrows(PropertyValueException.class, () -> projectService.setStatus(id, ProjectStatus.TESTING));
    }

    @Test
    public void setIncorrectDownStatusExistTest() {
        Project inputProject = genDevelopingProject();
        Integer id = inputProject.getId();
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.of(inputProject));
        Assertions.assertThrows(PropertyValueException.class, () -> projectService.setStatus(id, ProjectStatus.DRAFT ));
    }

    @Test
    public void setStatusNotExist() {
        Integer id = genRandomId();
        when(projectRepositoryJpa.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> projectService.setStatus(id, any()));
    }

    @Test
    public void findTest() throws JsonProcessingException {
        Project project = genProject();
        List<Project> projects = new ArrayList<>();
        projects.add(project);

        List<ProjectDto> findResult = new ArrayList<>();
        findResult.add(mapToDto(project));

        when(projectRepositoryJpa.findAllByTextAndStatuses(project.getTitle(), List.of(ProjectStatus.DRAFT))).thenReturn(projects);
        when(mapper.toListDto(projects)).thenReturn(findResult);

        List<ProjectDto> actualFindResult = projectService.find(project.getTitle(), List.of(ProjectStatus.DRAFT));
        Assertions.assertEquals(findResult.hashCode(), actualFindResult.hashCode());
    }
}
