package com.digdes.java2023;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;

import java.util.Random;

import static org.instancio.Select.field;

public class ProjectOperations {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    protected Project genProject() {
        return Instancio.create(Project.class);
    }

    protected Project genDraftProject() {
        return Instancio.of(Project.class)
                .set(field(Project::getStatus), ProjectStatus.DRAFT)
                .create();
    }

    protected Project genDevelopingProject() {
        return Instancio.of(Project.class)
                .set(field(Project::getStatus), ProjectStatus.DEVELOPING)
                .create();
    }

    protected Project copyProject(Project project) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(project), Project.class);
    }

    protected Integer genRandomId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    protected ProjectDto mapToDto(Project project) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(project), ProjectDto.class);
    }
}
