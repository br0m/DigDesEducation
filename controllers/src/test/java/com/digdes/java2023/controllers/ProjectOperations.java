package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;

import java.util.Random;

import static org.instancio.Select.field;

public class ProjectOperations {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected static final ProjectDto blankTitleProject = Instancio.of(ProjectDto.class).set(field(ProjectDto::getTitle), " ").create();
    protected static final ProjectDto blankCodenameProject = Instancio.of(ProjectDto.class).set(field(ProjectDto::getCodename), " ").create();
    protected Project project;
    protected Project genProject() {
        return Instancio.create(Project.class);
    }

    protected Project genDraftProject() {
        return Instancio.of(Project.class)
                .set(field(Project::getStatus), ProjectStatus.DRAFT)
                .create();
    }

    protected Integer genRandomId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    protected ProjectDto mapToDto(Project project) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(project), ProjectDto.class);
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
