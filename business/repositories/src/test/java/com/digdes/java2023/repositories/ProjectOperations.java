package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.model.Project;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class ProjectOperations extends Init{

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
}
