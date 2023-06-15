package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public Project toEntity(ProjectDto projectDto) {
        log.debug("Map DTO to entity");
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto toDto(Project project) {
        log.debug("Map entity to DTO");
        return modelMapper.map(project, ProjectDto.class);
    }

    public List<ProjectDto> toListDto(List<Project> projects) {
        if(projects==null || projects.isEmpty())
            return null;
        log.debug("Map entity list to DTO list");
        return modelMapper.map(projects, new TypeToken<List<ProjectDto>>() {
        }.getType());
    }
}
