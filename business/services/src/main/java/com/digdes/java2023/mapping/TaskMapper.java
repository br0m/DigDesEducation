package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.task.FindTaskDto;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.model.Task;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    ModelMapper modelMapper = new ModelMapper();

    public Task toEntity(Object taskDto) {
        assert (taskDto instanceof TaskDto || taskDto instanceof FindTaskDto);
        return modelMapper.map(taskDto, Task.class);
    }

    public TaskViewDto toDto(Task task) {
        return modelMapper.map(task, TaskViewDto.class);
    }

    public List<TaskViewDto> toListDto(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty())
            return null;

        return modelMapper.map(tasks, new TypeToken<List<TaskViewDto>>() {
        }.getType());
    }
}
