package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.model.Project;
import org.instancio.Instancio;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.instancio.Select.field;

public class ProjectOperations extends Init{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    protected Project genProject() {
        return Instancio.create(Project.class);
    }

    protected Project genDraftProject() {
        return Instancio.of(Project.class)
                .ignore(field(Project::getId))
                .set(field(Project::getStatus), ProjectStatus.DRAFT)
                .create();
    }

    protected Project genDevelopingProject(String title) {
        return Instancio.of(Project.class)
                .ignore(field(Project::getId))
                .set(field(Project::getTitle), title)
                .set(field(Project::getStatus), ProjectStatus.DEVELOPING)
                .create();
    }

    protected Project genTestingProject(String title) {
        return Instancio.of(Project.class)
                .ignore(field(Project::getId))
                .set(field(Project::getTitle), title)
                .set(field(Project::getStatus), ProjectStatus.TESTING)
                .create();
    }

    protected Project genFinishedProject(String title) {
        return Instancio.of(Project.class)
                .ignore(field(Project::getId))
                .set(field(Project::getTitle), title)
                .set(field(Project::getStatus), ProjectStatus.FINISHED)
                .create();
    }

    protected Project copyProject(Project project) throws IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(project), Project.class);
    }
}
