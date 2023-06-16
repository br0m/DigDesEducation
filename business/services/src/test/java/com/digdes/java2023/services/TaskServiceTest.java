package com.digdes.java2023.services;

import com.digdes.java2023.TaskOperations;
import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.mapping.TaskMapper;
import com.digdes.java2023.model.*;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TaskRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.impl.EmailServiceImpl;
import com.digdes.java2023.services.impl.TaskServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest extends TaskOperations {

    @Spy
    TeamRepositoryJpa teamRepositoryJpa;
    @Spy
    ProjectRepositoryJpa projectRepositoryJpa;
    @Spy
    TaskRepositoryJpa taskRepositoryJpa;
    @Mock
    TaskMapper mapper;
    @Mock
    EmailServiceImpl emailService;
    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    public void createTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Task outputTask = copyTask(inputTask);
        outputTask.setId(null);
        outputTask.setStatus(TaskStatus.NEW);
        outputTask.setCreationDate(LocalDateTime.now());
        outputTask.setDeadline(outputTask.getCreationDate().plusHours(outputTask.getHoursCost()));

        TaskDto taskDto = mapToDto(inputTask);
        TaskViewDto taskViewDto = mapToViewDto(outputTask);

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        mockSecurityContext(inputTask);
        mockCheckTeamMembers(inputTask);
        when(mapper.toDto(inputTask)).thenReturn(taskViewDto);

        TaskViewDto actualTaskViewDto;
        try (MockedStatic<LocalDateTime> creationDate = mockStatic(LocalDateTime.class)) {
            creationDate.when(LocalDateTime::now).thenReturn(outputTask.getCreationDate());
            actualTaskViewDto = taskService.create(taskDto);
        }

        verify(emailService).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa).save(argThat(t -> t.hashCode() == outputTask.hashCode()));
        Assertions.assertEquals(taskViewDto.hashCode(), actualTaskViewDto.hashCode());
    }

    @Test
    public void createProjectNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        TaskDto taskDto = mapToDto(inputTask);
        String projectCodename = inputTask.getProject().getCodename();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.create(taskDto));
    }

    @Test
    public void createAuthorNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        TaskDto taskDto = mapToDto(inputTask);
        String projectCodename = inputTask.getProject().getCodename();
        Integer authorId = inputTask.getAuthor().getMember().getId();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.of(inputTask.getProject()));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorId, projectCodename)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.create(taskDto));
    }

    @Test
    public void createResponsibleMemberNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        TaskDto taskDto = mapToDto(inputTask);
        String projectCodename = inputTask.getProject().getCodename();
        Integer authorId = inputTask.getAuthor().getMember().getId();
        Integer responsibleMemberId = inputTask.getResponsibleMember().getId();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.of(inputTask.getProject()));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorId, projectCodename)).thenReturn(Optional.of(inputTask.getAuthor()));
        when(teamRepositoryJpa.findById(responsibleMemberId)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.create(taskDto));
    }

    @Test
    public void createResponsibleMemberRemovedTest() throws JsonProcessingException {
        Task inputTask = genTask();
        inputTask.getResponsibleMember().getMember().setStatus(MemberStatus.REMOVED);
        TaskDto taskDto = mapToDto(inputTask);

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        mockSecurityContext(inputTask);
        mockCheckTeamMembers(inputTask);

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.create(taskDto));
    }

    @Test
    public void updateTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Task oldTask = genTask();
        Integer id = genRandomId();
        Task outputTask = copyTask(inputTask);
        outputTask.setId(id);
        outputTask.setDeadline(oldTask.getCreationDate().plusHours(inputTask.getHoursCost()));
        outputTask.setLastchangeDate(LocalDateTime.now());

        TaskDto taskDto = mapToDto(inputTask);
        TaskViewDto taskViewDto = mapToViewDto(outputTask);

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(oldTask));
        mockSecurityContext(inputTask);
        mockCheckTeamMembers(inputTask);
        when(taskRepositoryJpa.update(outputTask.getTitle(), outputTask.getDescription(), outputTask.getResponsibleMember(), outputTask.getHoursCost(), outputTask.getDeadline(), outputTask.getAuthor(), outputTask.getLastchangeDate(), outputTask.getProject(), id)).thenReturn(1);
        when(mapper.toDto(outputTask)).thenReturn(taskViewDto);

        TaskViewDto actualTaskViewDto;
        try (MockedStatic<LocalDateTime> creationDate = mockStatic(LocalDateTime.class)) {
            creationDate.when(LocalDateTime::now).thenReturn(outputTask.getLastchangeDate());
            actualTaskViewDto = taskService.update(taskDto, id);
        }

        verify(emailService).sendMessage(any(), anyBoolean());
        Assertions.assertEquals(taskViewDto.hashCode(), actualTaskViewDto.hashCode());
    }

    @Test
    public void updateTaskNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Integer id = genRandomId();
        TaskDto taskDto = mapToDto(inputTask);

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).update(any(), any(), any(), any(), any(), any(), any(), any(), any());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> taskService.update(taskDto, id));
    }

    @Test
    public void updateProjectNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Task oldTask = genTask();
        Integer id = genRandomId();
        TaskDto taskDto = mapToDto(inputTask);

        String projectCodename = inputTask.getProject().getCodename();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(oldTask));
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).update(any(), any(), any(), any(), any(), any(), any(), any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.update(taskDto, id));
    }

    @Test
    public void updateAuthorNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Task oldTask = genTask();
        Integer id = genRandomId();
        TaskDto taskDto = mapToDto(inputTask);

        String projectCodename = inputTask.getProject().getCodename();
        Integer authorId = inputTask.getAuthor().getMember().getId();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(oldTask));
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.of(inputTask.getProject()));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorId, projectCodename)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).update(any(), any(), any(), any(), any(), any(), any(), any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.update(taskDto, id));
    }

    @Test
    public void updateResponsibleMemberNotExistTest() throws JsonProcessingException {
        Task inputTask = genTask();
        Task oldTask = genTask();
        Integer id = genRandomId();
        TaskDto taskDto = mapToDto(inputTask);

        String projectCodename = inputTask.getProject().getCodename();
        Integer authorId = inputTask.getAuthor().getMember().getId();
        Integer responsibleMemberId = inputTask.getResponsibleMember().getId();

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(oldTask));
        mockSecurityContext(inputTask);
        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.of(inputTask.getProject()));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorId, projectCodename)).thenReturn(Optional.of(inputTask.getAuthor()));
        when(teamRepositoryJpa.findById(responsibleMemberId)).thenReturn(Optional.empty());

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).update(any(), any(), any(), any(), any(), any(), any(), any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.update(taskDto, id));
    }

    @Test
    public void updateResponsibleMemberRemovedTest() throws JsonProcessingException {
        Task inputTask = genTask();
        inputTask.getResponsibleMember().getMember().setStatus(MemberStatus.REMOVED);
        Task oldTask = genTask();
        Integer id = genRandomId();
        TaskDto taskDto = mapToDto(inputTask);

        when(mapper.toEntity(taskDto)).thenReturn(inputTask);
        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(oldTask));
        mockSecurityContext(inputTask);
        mockCheckTeamMembers(inputTask);

        verify(emailService, times(0)).sendMessage(any(), anyBoolean());
        verify(taskRepositoryJpa, times(0)).update(any(), any(), any(), any(), any(), any(), any(), any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.update(taskDto, id));
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"NEW", "WORKING", "FINISHED"})
    public void setStatusTest(TaskStatus status) throws JsonProcessingException {
        Task task = genTaskWithStatus(status);
        Integer id = task.getId();
        TaskStatus newStatus = getNewStatus(status, 1);
        TaskViewDto taskViewDto = mapToViewDto(task);

        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(task));
        when(taskRepositoryJpa.setStatus(newStatus, id)).thenReturn(1);
        when(mapper.toDto(task)).thenReturn(taskViewDto);

        TaskViewDto actualTaskViewDto = taskService.setStatus(id, newStatus);
        taskViewDto.setStatus(newStatus);
        verify(taskRepositoryJpa).setStatus(newStatus, id);
        Assertions.assertEquals(taskViewDto.hashCode(), actualTaskViewDto.hashCode());
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"NEW", "WORKING"})
    public void setIncorrectUpStatusTest(TaskStatus status) {
        Task task = genTaskWithStatus(status);
        Integer id = task.getId();
        TaskStatus newStatus = getNewStatus(status, 2);

        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(task));

        verify(taskRepositoryJpa, times(0)).setStatus(any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.setStatus(id, newStatus));
    }

    @ParameterizedTest
    @EnumSource(value = TaskStatus.class, names = {"WORKING","FINISHED", "CLOSED"})
    public void setIncorrectDownStatusTest(TaskStatus status) {
        Task task = genTaskWithStatus(status);
        Integer id = task.getId();
        TaskStatus newStatus = getNewStatus(status, -1);

        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(task));

        verify(taskRepositoryJpa, times(0)).setStatus(any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.setStatus(id, newStatus));
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    public void setIncorrectTheSameStatusTest(TaskStatus status) {
        Task task = genTaskWithStatus(status);
        Integer id = task.getId();

        when(taskRepositoryJpa.findById(id)).thenReturn(Optional.of(task));

        verify(taskRepositoryJpa, times(0)).setStatus(any(), any());
        Assertions.assertThrows(PropertyValueException.class, () -> taskService.setStatus(id, status));
    }

    @Test
    public void findTest() throws JsonProcessingException {
        Task task = genTask();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        FindTaskDto findTaskDto = mapToFindDto(task);
        TaskViewDto taskViewDto = mapToViewDto(task);
        List<TaskViewDto> taskViewDtoList = new ArrayList<>();
        taskViewDtoList.add(taskViewDto);

        when(mapper.toEntity(findTaskDto)).thenReturn(task);
        when(taskRepositoryJpa.findAll((Specification<Task>) any(), (Sort) any())).thenReturn(tasks);
        when(mapper.toListDto(tasks)).thenReturn(taskViewDtoList);

        List<TaskViewDto> actualTaskViewDtoList = taskService.find(findTaskDto);
        Assertions.assertEquals(taskViewDtoList.hashCode(), actualTaskViewDtoList.hashCode());
    }

    private void mockSecurityContext(Task inputTask) {
        MemberDetails memberDetails = new MemberDetails(inputTask.getAuthor().getMember());

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(memberDetails);
    }

    private void mockCheckTeamMembers(Task inputTask) {
        String projectCodename = inputTask.getProject().getCodename();
        Integer authorId = inputTask.getAuthor().getMember().getId();
        Integer responsibleMemberId = inputTask.getResponsibleMember().getId();

        when(projectRepositoryJpa.findByCodename(projectCodename)).thenReturn(Optional.of(inputTask.getProject()));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(authorId, projectCodename)).thenReturn(Optional.of(inputTask.getAuthor()));
        when(teamRepositoryJpa.findById(responsibleMemberId)).thenReturn(Optional.of(inputTask.getResponsibleMember()));
    }

    private TaskStatus getNewStatus(TaskStatus status, int diff) {
        List<TaskStatus> statuses = new LinkedList<>(List.of(TaskStatus.NEW, TaskStatus.WORKING, TaskStatus.FINISHED, TaskStatus.CLOSED));
        return statuses.get(status.ordinal() + diff);
    }
}
