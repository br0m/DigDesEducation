package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskMapper {

    private final ModelMapper modelMapper;

    public Task toEntity(Object taskDto) {
        assert (taskDto instanceof TaskDto || taskDto instanceof FindTaskDto);
        log.debug("Map DTO to entity");
        return modelMapper.map(taskDto, Task.class);
    }

    public TaskViewDto toDto(Task task) {
        log.debug("Map entity to DTO");
        return modelMapper.map(task, TaskViewDto.class);
    }

    public List<TaskViewDto> toListDto(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty())
            return null;
        log.debug("Map entity list to DTO list");
        return modelMapper.map(tasks, new TypeToken<List<TaskViewDto>>() {
        }.getType());
    }
}
