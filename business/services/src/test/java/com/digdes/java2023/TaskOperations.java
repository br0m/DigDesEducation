package com.digdes.java2023;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.instancio.Instancio;

import java.time.LocalDateTime;
import java.util.Random;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

public class TaskOperations {
    private final static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    protected Task genTask() {
        Project project = Instancio.create(Project.class);
        Task task = Instancio.of(Task.class)
                .ignore(field(Task::getDeadline))
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .set(field(TeamMember::getProject), project)
                .set(field(Task::getProject), project)
                .supply(all(LocalDateTime.class), t -> LocalDateTime.now())
                .create();

        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));
        return task;
    }

    protected Task genTaskWithoutDateTime() {
        Project project = Instancio.create(Project.class);
        return Instancio.of(Task.class)
                .ignore(field(Member::getPassword))
                .set(field(TeamMember::getProject), project)
                .set(field(Task::getProject), project)
                .ignore(all(LocalDateTime.class))
                .create();
    }

    protected Task genTaskWithStatus(TaskStatus status) {
        Project project = Instancio.create(Project.class);
        Task task = Instancio.of(Task.class)
                .ignore(field(Task::getDeadline))
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .set(field(TeamMember::getProject), project)
                .set(field(Task::getProject), project)
                .set(field(Task::getStatus), status)
                .supply(all(LocalDateTime.class), t -> LocalDateTime.now())
                .create();

        task.setDeadline(task.getCreationDate().plusHours(task.getHoursCost()));
        return task;
    }

    protected Integer genRandomId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    protected TaskDto mapToDto(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), TaskDto.class);
    }

    protected TaskViewDto mapToViewDto(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), TaskViewDto.class);
    }

    protected FindTaskDto mapToFindDto(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), FindTaskDto.class);
    }

    protected Task copyTask(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), Task.class);
    }
}
