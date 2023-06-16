package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.mapping.ProjectMapper;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.config.SecurityConfig;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.services.impl.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {SecurityConfig.class, ProjectController.class, ProjectServiceImpl.class, Project.class, GlobalExceptionHandler.class})
@WebMvcTest
@AutoConfigureMockMvc
@WithMockUser
public class ProjectControllerTest extends ProjectOperations {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectMapper mapper;
    @MockBean
    private ProjectRepositoryJpa projectRepositoryJpa;
    @Autowired
    public ProjectControllerTest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Test
    public void createTest() throws Exception {
        Project inputProject = genProject();
        ProjectDto outputProject = mapToDto(inputProject);
        outputProject.setId(genRandomId());
        outputProject.setStatus(ProjectStatus.DRAFT);

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(mapper.toDto(inputProject)).thenReturn(outputProject);

        mockMvc.perform(post("/project/save")
                        .content(toJson(inputProject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputProject)));
    }

    @Test
    public void createExistTest() throws Exception {
        Project inputProject = genProject();

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findAll()).thenReturn(List.of(inputProject));

        mockMvc.perform(post("/project/save")
                        .content(toJson(inputProject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createInvalidDtoTest(ProjectDto projectDto) throws Exception {
        mockMvc.perform(post("/project/save")
                        .content(toJson(projectDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateExistTest() throws Exception {
        Project inputProject = genProject();
        ProjectDto outputProject = mapToDto(inputProject);
        Integer id = inputProject.getId();

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.of(inputProject));
        when(projectRepositoryJpa.update(any(), any(), any(), any())).thenReturn(1);
        when(mapper.toDto(any())).thenReturn(outputProject);

        mockMvc.perform(put("/project/{id}", id)
                        .content(toJson(inputProject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputProject)));
    }

    @Test
    public void updateNotExistTest() throws Exception {
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/project/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateNotUniqueCodenameTest() throws Exception {
        Project inputProject = genProject();

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.of(genProject()));
        when(projectRepositoryJpa.findAll()).thenReturn(List.of(inputProject));

        mockMvc.perform(put("/project/{id}", inputProject.getId()))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void updateInvalidDtoTest(ProjectDto projectDto) throws Exception {
        mockMvc.perform(put("/project/{id}", projectDto.getId())
                        .content(toJson(projectDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void setStatusTest() throws Exception {
        Project inputProject = genDraftProject();
        ProjectDto outputProject = mapToDto(inputProject);
        Integer id = inputProject.getId();
        outputProject.setStatus(ProjectStatus.DEVELOPING);

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.of(inputProject));
        when(projectRepositoryJpa.setStatus(any(), any())).thenReturn(1);
        when(mapper.toDto(any())).thenReturn(outputProject);

        mockMvc.perform(patch("/project/{id}/{status}", id, outputProject.getStatus()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputProject)));
    }

    @Test
    public void setWrongStatusTest() throws Exception {
        Project inputProject = genDraftProject();
        ProjectDto outputProject = mapToDto(inputProject);
        Integer id = inputProject.getId();
        outputProject.setStatus(ProjectStatus.TESTING);

        when(mapper.toEntity(any())).thenReturn(inputProject);
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.of(inputProject));
        when(projectRepositoryJpa.setStatus(any(), any())).thenReturn(1);
        when(mapper.toDto(any())).thenReturn(outputProject);

        mockMvc.perform(patch("/project/{id}/{status}", id, outputProject.getStatus()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void setStatusNotExistTest() throws Exception {
        when(projectRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(patch("/project/{id}/{status}", genRandomId(), ProjectStatus.DEVELOPING))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidSetStatusParams")
    public void setStatusInvalidParamsTest(Integer id, ProjectStatus status) throws Exception {
        mockMvc.perform(patch("/project/{id}/{status}", id, status))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findTest() throws Exception {
        List<ProjectDto> projectsDto = new ArrayList<>();
        Project project = genDraftProject();
        projectsDto.add(mapToDto(project));

        when(mapper.toListDto(any())).thenReturn(projectsDto);

        mockMvc.perform(get("/project/find")
                        .param("text", project.getTitle())
                        .param("statuses", ProjectStatus.DRAFT.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(projectsDto)));
    }

    @ParameterizedTest
    @MethodSource("invalidFindParams")
    public void findInvalidParamsTest(String text, List<String> statuses) throws Exception {
        mockMvc.perform(get("/project/find")
                        .param("text", text)
                        .param("statuses", (statuses!=null && statuses.size()!=0) ? statuses.get(0) : ""))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.of(blankTitleProject),
                Arguments.of(blankCodenameProject));
    }

    private static Stream<Arguments> invalidFindParams() {
        return Stream.of(
                Arguments.of(null, List.of(ProjectStatus.DRAFT.toString())),
                Arguments.of("1", List.of(ProjectStatus.DRAFT.toString())),
                Arguments.of("12", List.of(ProjectStatus.DRAFT.toString())),
                Arguments.of("123", null),
                Arguments.of("123", new ArrayList<String>()),
                Arguments.of("123", List.of("QWERTY")));
    }

    private static Stream<Arguments> invalidSetStatusParams() {
        return Stream.of(
                Arguments.of(null, ProjectStatus.DRAFT),
                Arguments.of(1, null));
    }
}
