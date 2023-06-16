package com.digdes.java2023.mapping;

import com.digdes.java2023.ProjectOperations;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProjectMappingTest extends ProjectOperations {

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    ProjectMapper projectMapper;

    @Test
    public void DtoToEntityTest() throws JsonProcessingException {
        Project project = genProject();
        ProjectDto projectDto = mapToDto(project);

        Project actualProject = projectMapper.toEntity(projectDto);
        verify(modelMapper).map(projectDto, Project.class);

        Assertions.assertEquals(project.hashCode(), actualProject.hashCode());
    }

    @Test
    public void EntityToDtoTest() throws JsonProcessingException {
        Project project = genProject();
        ProjectDto projectDto = mapToDto(project);

        ProjectDto actualProjectDto = projectMapper.toDto(project);
        verify(modelMapper).map(project, ProjectDto.class);

        Assertions.assertEquals(projectDto.hashCode(), actualProjectDto.hashCode());
    }

    @Test
    public void ListEntityToListDtoTest() throws JsonProcessingException {
        List<Project> projects = Instancio.ofList(Project.class).size(5).create();
        List<ProjectDto> projectsDto = new ArrayList<>();

        for (Project project : projects) {
            projectsDto.add(mapToDto(project));
        }

        List<ProjectDto> actualProjectsDto = projectMapper.toListDto(projects);
        verify(modelMapper).map(projects, new TypeToken<List<ProjectDto>>() {
        }.getType());

        Assertions.assertEquals(projectsDto.hashCode(), actualProjectsDto.hashCode());
    }
}
