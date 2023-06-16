package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.config.ModelConfig;
import com.digdes.java2023.repositories.config.RepositoryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {RepositoryConfig.class, ModelConfig.class, ProjectRepositoryJpa.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryJpaTest extends ProjectOperations {
    @Autowired
    private ProjectRepositoryJpa projectRepository;

    @Test
    public void createTest() {
        Project project = genProject();
        project = projectRepository.save(project);

        Optional<Project> actualProject = projectRepository.findById(project.getId());
        assert actualProject.isPresent();
        Assertions.assertEquals(project.hashCode(), actualProject.get().hashCode());
    }

    @Test
    public void updateTest() {
        Project project = genProject();
        project = projectRepository.save(project);

        Project updateProject = genProject();
        updateProject.setId(project.getId());
        updateProject = projectRepository.save(updateProject);

        Optional<Project> actualUpdateProject = projectRepository.findById(updateProject.getId());
        assert actualUpdateProject.isPresent();
        Assertions.assertEquals(updateProject.hashCode(), actualUpdateProject.hashCode());
    }

    @Test
    public void findByIdTest() {
        Project project = genProject();
        project = projectRepository.save(project);
        Optional<Project> findProject = projectRepository.findById(project.getId());
        assert findProject.isPresent();
        Assertions.assertEquals(project.hashCode(), findProject.get().hashCode());
    }

    @Test
    public void findByCodenameTest() {
        Project project = genProject();
        project = projectRepository.save(project);
        Optional<Project> findProject = projectRepository.findByCodename(project.getCodename());
        assert findProject.isPresent();
        Assertions.assertEquals(project.hashCode(), findProject.get().hashCode());
    }

    @Test
    public void findAllByTextAndStatusesTest() {
        Project draftProject = genDraftProject();
        Project testingProject = genTestingProject(draftProject.getTitle());
        Project developingProject = genDevelopingProject(draftProject.getTitle());
        Project finishedProject = genFinishedProject(draftProject.getTitle());

        projectRepository.saveAll(List.of(draftProject, testingProject, developingProject, finishedProject));

        findProjects(draftProject.getCodename(), List.of(ProjectStatus.DRAFT, ProjectStatus.TESTING), List.of(draftProject));
        findProjects(draftProject.getTitle(), List.of(ProjectStatus.DRAFT, ProjectStatus.TESTING), List.of(draftProject, testingProject));
        findProjects(developingProject.getCodename(), List.of(ProjectStatus.DEVELOPING, ProjectStatus.FINISHED), List.of(developingProject));
        findProjects(developingProject.getTitle(), List.of(ProjectStatus.DEVELOPING, ProjectStatus.FINISHED), List.of(developingProject, finishedProject));
    }


    @Test
    public void setStatusTest() {
        Project project = genDraftProject();
        project = projectRepository.save(project);

        int count = projectRepository.setStatus(project.getId(), ProjectStatus.TESTING);
        project.setStatus(ProjectStatus.TESTING);
        Optional<Project> actualProject = projectRepository.findById(project.getId());

        assert actualProject.isPresent();
        Assertions.assertEquals(1, count);
        Assertions.assertEquals(project.hashCode(), actualProject.hashCode());
    }

    private void findProjects(String text, List<ProjectStatus> statuses, List<Project> expectedProjects) {
        List<Project> actualProjects = projectRepository.findAllByTextAndStatuses(text, statuses);
        Assertions.assertEquals(expectedProjects.size(), actualProjects.size());
        Assertions.assertEquals(expectedProjects.hashCode(), actualProjects.hashCode());
    }
}
