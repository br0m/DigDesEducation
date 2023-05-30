package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskSpecification {
    private final Task task;
    private final TaskTimeFindParam deadlinePeriod;
    private final TaskTimeFindParam creationDatePeriod;

    public TaskSpecification(Task task, FindTaskDto findTaskDto) {
        this.task = task;
        this.deadlinePeriod = findTaskDto.getDeadlinePeriod();
        this.creationDatePeriod = findTaskDto.getCreationPeriod();
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

    public Specification<Task> buildSpecification() {
        return titleEquals(task.getTitle())
                .and(teamMemberEquals(task.getResponsibleMember(), "responsibleMember"))
                .and(statusEquals(task.getStatus()))
                .and(teamMemberEquals(task.getAuthor(), "author"))
                .and(dateTimeSpec(task.getDeadline(), deadlinePeriod, "deadline"))
                .and(dateTimeSpec(task.getCreationDate(), creationDatePeriod, "creationDate"));

    }
}
