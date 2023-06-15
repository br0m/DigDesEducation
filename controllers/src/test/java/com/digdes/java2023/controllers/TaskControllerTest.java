package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.mapping.TaskMapper;
import com.digdes.java2023.model.*;
import com.digdes.java2023.model.config.SecurityConfig;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TaskRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.impl.EmailServiceImpl;
import com.digdes.java2023.services.impl.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.instancio.Select.all;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {SecurityConfig.class, TaskController.class, TaskServiceImpl.class, Task.class, GlobalExceptionHandler.class})
@WebMvcTest
@AutoConfigureMockMvc
@WithMockUser
public class TaskControllerTest extends TaskOperations {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskMapper mapper;
    @MockBean
    private TeamRepositoryJpa teamRepositoryJpa;
    @MockBean
    private ProjectRepositoryJpa projectRepositoryJpa;
    @MockBean
    private TaskRepositoryJpa taskRepositoryJpa;
    @MockBean
    private EmailServiceImpl emailService;

    @Autowired
    public TaskControllerTest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @BeforeEach
    public void getTask() {
        project = Instancio.create(Project.class);
        responsibleMember = Instancio.of(Member.class)
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .create();
        authorMember = Instancio.of(Member.class)
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .create();

        responsibleTeamMember = Instancio.of(TeamMember.class)
                .set(field(TeamMember::getMember), responsibleMember)
                .set(field(TeamMember::getProject), project)
                .create();
        authorTeamMember = Instancio.of(TeamMember.class)
                .set(field(TeamMember::getMember), authorMember)
                .set(field(TeamMember::getProject), project)
                .create();

        task = Instancio.of(Task.class)
                .ignore(all(LocalDateTime.class))
                .set(field(Task::getProject), project)
                .set(field(Task::getResponsibleMember), responsibleTeamMember)
                .set(field(Task::getAuthor), authorTeamMember)
                .generate(field(Task::getHoursCost), gen -> gen.ints().range(1, 10))
                .create();

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails memberDetails = new MemberDetails(authorMember);
        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails, "password", memberDetails.getAuthorities());
        context.setAuthentication(auth);
    }

    @Test
    public void createTest() throws Exception {
        TaskDto taskDto = mapToDto(task);
        task.setStatus(TaskStatus.NEW);
        task.setCreationDate(LocalDateTime.now());
        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));
        TaskViewDto taskViewDto = mapToViewDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        mockCheckTeamMembers();
        when(mapper.toDto(task)).thenReturn(taskViewDto);

        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(taskViewDto)));

    }

    @Test
    public void createProjectNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.empty());

        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createAuthorNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorMember.getId(), project.getCodename())).thenReturn(Optional.empty());

        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createResponsibleMemberNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorMember.getId(), project.getCodename())).thenReturn(Optional.of(authorTeamMember));
        when(teamRepositoryJpa.findById(responsibleTeamMember.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createResponsibleMemberRemovedTest() throws Exception {
        responsibleMember.setStatus(MemberStatus.REMOVED);
        TaskDto taskDto = mapToDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        mockCheckTeamMembers();

        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createInvalidDtoTest(TaskDto taskDto) throws Exception {
        mockMvc.perform(post("/task/save")
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateTest() throws Exception {
        TaskDto taskDto = mapToDto(task);
        Task oldTask = Instancio.create(Task.class);
        task.setDeadline(oldTask.getCreationDate().plusHours(task.getHoursCost()));
        task.setLastchangeDate(LocalDateTime.now());
        TaskViewDto taskViewDto = mapToViewDto(task);
        taskViewDto.setStatus(oldTask.getStatus());

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(oldTask));
        mockCheckTeamMembers();
        when(taskRepositoryJpa.update(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);
        when(mapper.toDto(task)).thenReturn(taskViewDto);

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(taskViewDto)));
    }

    @Test
    public void updateTaskNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProjectNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);
        Task oldTask = Instancio.create(Task.class);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(oldTask));
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAuthorNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);
        Task oldTask = Instancio.create(Task.class);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(oldTask));
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorMember.getId(), project.getCodename())).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateResponsibleMemberNotExistTest() throws Exception {
        TaskDto taskDto = mapToDto(task);
        Task oldTask = Instancio.create(Task.class);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(oldTask));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorMember.getId(), project.getCodename())).thenReturn(Optional.of(authorTeamMember));
        when(teamRepositoryJpa.findById(responsibleTeamMember.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateResponsibleMemberRemovedTest() throws Exception {
        responsibleMember.setStatus(MemberStatus.REMOVED);
        TaskDto taskDto = mapToDto(task);
        Task oldTask = Instancio.create(Task.class);

        when(mapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(oldTask));
        mockCheckTeamMembers();

        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void updateInvalidDtoTest(TaskDto taskDto) throws Exception {
        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(taskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"NEW", "WORKING", "FINISHED"})
    public void setStatusTest(TaskStatus status) throws Exception {
        task.setStatus(status);
        TaskStatus newStatus = getNewStatus(status, 1);
        TaskViewDto taskViewDto = mapToViewDto(task);
        taskViewDto.setStatus(newStatus);

        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepositoryJpa.setStatus(newStatus, task.getId())).thenReturn(1);
        when(mapper.toDto(task)).thenReturn(taskViewDto);

        mockMvc.perform(patch("/task/{id}/{status}", task.getId(), newStatus))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(taskViewDto)));
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"NEW", "WORKING"})
    public void setWrongUpStatusTest(TaskStatus status) throws Exception {
        task.setStatus(status);
        TaskStatus newStatus = getNewStatus(status, 2);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(task));
        mockMvc.perform(patch("/task/{id}/{status}", task.getId(), newStatus))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"FINISHED", "CLOSED"})
    public void setWrongDownStatusTest(TaskStatus status) throws Exception {
        task.setStatus(status);
        TaskStatus newStatus = getNewStatus(status, -2);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(task));
        mockMvc.perform(patch("/task/{id}/{status}", task.getId(), newStatus))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    public void setTheSameStatusTest(TaskStatus status) throws Exception {
        task.setStatus(status);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(task));
        mockMvc.perform(patch("/task/{id}/{status}", task.getId(), status))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("setStatusInvalidParams")
    public void setStatusInvalidParamsTest(Integer id, TaskStatus status) throws Exception {
        task.setStatus(status);
        when(taskRepositoryJpa.findById(task.getId())).thenReturn(Optional.of(task));
        mockMvc.perform(patch("/task/{id}/{status}", id, status))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findTest() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        TaskViewDto taskViewDto = mapToViewDto(task);
        List<TaskViewDto> taskViewDtoList = new ArrayList<>();
        taskViewDtoList.add(taskViewDto);
        FindTaskDto findTaskDto = new FindTaskDto();

        when(mapper.toEntity(findTaskDto)).thenReturn(task);
        when(taskRepositoryJpa.findAll((Specification<Task>) any(), (Sort) any())).thenReturn(tasks);
        when(mapper.toListDto(tasks)).thenReturn(taskViewDtoList);

        mockMvc.perform(post("/task/find")
                        .content(toJson(findTaskDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(taskViewDtoList)));
    }

    private TaskStatus getNewStatus(TaskStatus status, int diff) {
        List<TaskStatus> statuses = new LinkedList<>(List.of(TaskStatus.NEW, TaskStatus.WORKING, TaskStatus.FINISHED, TaskStatus.CLOSED));
        return statuses.get(status.ordinal() + diff);
    }

    private void mockCheckTeamMembers() {
        when(projectRepositoryJpa.findByCodename(project.getCodename())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorMember.getId(), project.getCodename())).thenReturn(Optional.of(authorTeamMember));
        when(teamRepositoryJpa.findById(responsibleTeamMember.getId())).thenReturn(Optional.of(responsibleTeamMember));
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.of(blankTitleDto),
                Arguments.of(nullProjectDto),
                Arguments.of(wrongHoursCostDto));
    }

    private static Stream<Arguments> setStatusInvalidParams() {
        return Stream.of(
                Arguments.of(null, TaskStatus.NEW),
                Arguments.of(1, null));
    }
}
