package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.model.Project;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public Project toEntity(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto toDto(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

    public List<ProjectDto> toListDto(List<Project> projects) {
        return modelMapper.map(projects, new TypeToken<List<ProjectDto>>() {
        }.getType());
    }
}
