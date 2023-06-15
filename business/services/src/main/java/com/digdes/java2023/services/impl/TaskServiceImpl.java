package com.digdes.java2023.services.impl;

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
import com.digdes.java2023.repositories.TaskSpecification;
import com.digdes.java2023.services.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
@Validated
public class TaskServiceImpl implements TaskService {

    private final TaskRepositoryJpa taskRepositoryJpa;
    private final TeamRepositoryJpa teamRepositoryJpa;
    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final TaskMapper mapper;
    private final EmailServiceImpl emailService;
    private final boolean USE_FAKE_EMAIL = false;

    @Override
    public TaskViewDto create(@Valid TaskDto taskDto) {
        log.info("Creating new member");
        log.debug(taskDto.toString());

        Task task = mapper.toEntity(taskDto);
        Member member = getAuthorMember();
        checkTeamMembers(member, task);

        task.setId(null);
        task.setStatus(TaskStatus.NEW);
        task.setCreationDate(LocalDateTime.now());
        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));

        taskRepositoryJpa.save(task);
        if(!task.getResponsibleMember().getMember().getEmail().isBlank())
            emailService.sendMessage(task, USE_FAKE_EMAIL);

        log.info("New task successfully created");
        log.debug(task.toString());
        return mapper.toDto(task);
    }

    @Override
    public TaskViewDto update(@Valid TaskDto taskDto, @NotNull Integer id) {
        log.info("Updating task with id = " + id);
        log.debug(taskDto!=null ? taskDto.toString() : null);

        Task task = mapper.toEntity(taskDto);
        Task oldTask = getTaskById(id);
        Member member = getAuthorMember();
        checkTeamMembers(member, task);

        task.setId(id);
        task.setDeadline(oldTask.getCreationDate().plusHours(task.getHoursCost()));
        task.setLastchangeDate(LocalDateTime.now());
        int count = taskRepositoryJpa.update(task.getTitle(), task.getDescription(), task.getResponsibleMember(), task.getHoursCost(), task.getDeadline(), task.getAuthor(), task.getLastchangeDate(), task.getProject(), id);

        if(!Objects.equals(oldTask.getResponsibleMember().getId(), task.getResponsibleMember().getId()) && !task.getResponsibleMember().getMember().getEmail().isBlank())
            emailService.sendMessage(task, USE_FAKE_EMAIL);

        log.info("Task with id = " + id + " successfully updated");
        log.debug(task.toString());
        return dtoOrNull(count, task);
    }

    @Override
    public TaskViewDto setStatus(@NotNull Integer id, @NotNull TaskStatus status) {
        log.info("Setting new status = " + status + " for task with id = " + id);

        Task task = getTaskById(id);
        if (status.ordinal() - task.getStatus().ordinal() != 1) {
            log.warn("New status is not according workflow");
            throw new PropertyValueException("Not according workflow", "task", "status");
        }

        task.setStatus(status);
        int count = taskRepositoryJpa.setStatus(status, id);
        log.info("Status successfully modified");
        log.debug(task.toString());
        return dtoOrNull(count, task);
    }

    @Override
    public List<TaskViewDto> find(FindTaskDto findTaskDto) {
        log.info("Searching tasks");
        log.debug(findTaskDto.toString());
        Task task = mapper.toEntity(findTaskDto);
        TaskSpecification taskSpecification = new TaskSpecification(task, findTaskDto);
        List<Task> tasks = taskRepositoryJpa.findAll(taskSpecification.buildSpecification(), Sort.by(Sort.Direction.DESC, "creationDate"));
        log.info("Search completed");
        log.debug(tasks.toString());
        return mapper.toListDto(tasks);
    }

    private Task getTaskById(Integer id) {
        Task task = taskRepositoryJpa.findById(id).orElse(null);
        if(task==null) {
            log.warn("Task with id = " + id + " not found");
            throw new ObjectNotFoundException(id, "task");
        }
        return task;
    }

    private TaskViewDto dtoOrNull(int count, Task task) {
        if(count==1) {
            log.info("Operation completed successfully");
            return mapper.toDto(task);
        }
        else {
            log.info("Operation failed");
            return null;
        }
    }

    private void checkTeamMembers(Member member, Task task) {
        String projectCodename = task.getProject().getCodename();
        Integer memberId = member.getId();
        Integer responsibleMemberId = task.getResponsibleMember() != null ? task.getResponsibleMember().getId() : null;

        Project project = projectRepositoryJpa.findByCodename(projectCodename).orElse(null);
        if(project==null) {
            log.warn("No such project with codename = " + projectCodename);
            throw new PropertyValueException("No such project", "task", "project.codename");
        }
        task.setProject(project);

        TeamMember author = teamRepositoryJpa.findByMemberIdAndProjectCodename(memberId, projectCodename).orElse(null);
        if(author==null) {
            log.warn("Author is not project team member");
            throw new PropertyValueException("You are not project team member", "task", "project");
        }
        task.setAuthor(author);

        if (responsibleMemberId != null) {
            TeamMember responsibleMember = teamRepositoryJpa.findById(responsibleMemberId).orElse(null);
            if(responsibleMember==null) {
                log.warn("No such responsible member");
                throw new PropertyValueException("No such team member", "task", "responsibleMember");
            }
            if (responsibleMember.getMember().getStatus() == MemberStatus.REMOVED) {
                log.warn("Responsible member already removed");
                log.debug(responsibleMember.toString());
                throw new PropertyValueException("Responsible member already removed", "task", "responsibleMember");
            }
            task.setResponsibleMember(responsibleMember);
        }
    }

    private Member getAuthorMember() {
        return ((MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
    }
}
