package com.digdes.java2023.services;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TaskService {

    TaskViewDto create(@Valid TaskDto taskDto);

    TaskViewDto update(@Valid TaskDto taskDto, @NotNull Integer id);

    TaskViewDto setStatus(@NotNull Integer id, @NotNull TaskStatus status);

    List<TaskViewDto> find(FindTaskDto findTaskDto);

}
