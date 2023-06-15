package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;

import static org.instancio.Select.field;
@RequiredArgsConstructor
public class TaskOperations {
    private final ObjectMapper objectMapper;
    protected Task task;
    protected Member authorMember;
    protected Member responsibleMember;
    protected Project project;
    protected TeamMember responsibleTeamMember;
    protected TeamMember authorTeamMember;

    protected static final TaskDto blankTitleDto = Instancio.of(TaskDto.class).ignore(field(TaskDto::getTitle)).create();
    protected static final TaskDto wrongHoursCostDto = Instancio.of(TaskDto.class).generate(field(TaskDto::getHoursCost), gen -> gen.ints().range(Integer.MIN_VALUE, 0)).create();
    protected static final TaskDto nullProjectDto = Instancio.of(TaskDto.class).ignore(field(TaskDto::getProject)).create();

    protected TaskDto mapToDto(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), TaskDto.class);
    }

    protected TaskViewDto mapToViewDto(Task task) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(task), TaskViewDto.class);
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
