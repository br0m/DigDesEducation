package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.mapping.TaskMapper;
import com.digdes.java2023.model.*;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TaskRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TaskServiceImpl implements TaskService {

    private final TaskRepositoryJpa taskRepositoryJpa;
    private final TeamRepositoryJpa teamRepositoryJpa;
    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final TaskMapper mapper;

    @Override
    public TaskViewDto create(@Valid TaskDto taskDto) {
        Task task = mapper.toEntity(taskDto);
        Member member = getAuthorMember();
        checkTeamMembers(member, task);
        task.setStatus(TaskStatus.NEW);
        task.setCreationDate(LocalDateTime.now());
        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));
        taskRepositoryJpa.save(task);
        return mapper.toDto(task);
    }

    @Override
    public TaskViewDto update(@Valid TaskDto taskDto, @NotNull Integer id) {
        Task task = mapper.toEntity(taskDto);
        Task oldTask = getTaskById(id);
        Member member = getAuthorMember();
        checkTeamMembers(member, task);
        task.setId(id);
        task.setDeadline(oldTask.getCreationDate().plusHours(task.getHoursCost()));
        task.setLastchangeDate(LocalDateTime.now());
        int count = taskRepositoryJpa.update(task.getTitle(), task.getDescription(), task.getResponsibleMember(), task.getHoursCost(), task.getDeadline(), task.getAuthor(), task.getLastchangeDate(), id);
        return dtoOrNull(count, task);
    }

    @Override
    public TaskViewDto setStatus(@NotNull Integer id, @NotNull TaskStatus status) {
        Task task = getTaskById(id);
        if (status.ordinal() - task.getStatus().ordinal() != 1)
            throw new PropertyValueException("Not according workflow", "task", "status");
        task.setStatus(status);
        int count = taskRepositoryJpa.setStatus(status, id);
        return dtoOrNull(count, task);
    }

    @Override
    public List<TaskViewDto> find(FindTaskDto findTaskDto) {
        Task task = mapper.toEntity(findTaskDto);
        Specification<Task> spec = buildSpecification(task, findTaskDto.getCreationPeriod(), findTaskDto.getDeadlinePeriod());
        List<Task> tasks = taskRepositoryJpa.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));
        return mapper.toListDto(tasks);
    }

    private Task getTaskById(Integer id) {
        return taskRepositoryJpa.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "task"));
    }

    private TaskViewDto dtoOrNull(int count, Task task) {
        return count == 1 ? mapper.toDto(task) : null;
    }

    private void checkTeamMembers(Member member, Task task) {

        String projectCodename = task.getProject().getCodename();
        Integer memberId = member.getId();
        Integer responsibleMemberId = task.getResponsibleMember() != null ? task.getResponsibleMember().getId() : null;

        Project project = projectRepositoryJpa.findByCodename(projectCodename).orElseThrow(() -> new PropertyValueException("No such project", "task", "project.codename"));
        task.setProject(project);

        TeamMember author = teamRepositoryJpa.findTeamMemberByMemberIdAndProjectCodename(memberId, projectCodename).orElseThrow(() -> new PropertyValueException("You are not project team member", "task", "project"));
        task.setAuthor(author);

        if (responsibleMemberId != null) {
            TeamMember responsibleMember = teamRepositoryJpa.findById(responsibleMemberId).orElseThrow(() -> new PropertyValueException("No such team member", "task", "responsibleMember"));
            if (responsibleMember.getMember().getStatus() == MemberStatus.REMOVED)
                throw new PropertyValueException("Responsible member already removed", "task", "responsibleMember");
        }
    }

    private Member getAuthorMember() {
        return ((MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
    }

    private Specification<Task> titleEquals(String title) {
        return title != null ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title) : Specification.where(null);
    }

    private Specification<Task> teamMemberEquals(TeamMember teamMember, String paramName) {
        return teamMember != null ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(paramName), teamMember.getId()) : Specification.where(null);
    }

    private Specification<Task> statusEquals(TaskStatus status) {
        return status != null ? (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status) : Specification.where(null);
    }

    private Specification<Task> dateTimeSpec(LocalDateTime localDateTime, TaskTimeFindParam taskTimeFindParam, String paramName) {
        switch (taskTimeFindParam) {
            case AFTER -> {
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(paramName), localDateTime);
            }
            case BEFORE -> {
                return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(paramName), localDateTime);
            }
            case EQUAL -> {
                return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(paramName), localDateTime);
            }
            default -> {
                return Specification.where(null);
            }
        }
    }

    private Specification<Task> buildSpecification(Task task, TaskTimeFindParam creationDateParam, TaskTimeFindParam deadlineParam) {
        return titleEquals(task.getTitle())
                .and(teamMemberEquals(task.getResponsibleMember(), "responsibleMember"))
                .and(statusEquals(task.getStatus()))
                .and(teamMemberEquals(task.getAuthor(), "author"))
                .and(dateTimeSpec(task.getDeadline(), deadlineParam, "deadline"))
                .and(dateTimeSpec(task.getCreationDate(), creationDateParam, "creationDate"));

    }
}
