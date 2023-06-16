package com.digdes.java2023.mapping;

import com.digdes.java2023.TaskOperations;
import com.digdes.java2023.dto.task.TaskDto;
import com.digdes.java2023.dto.task.TaskViewDto;
import com.digdes.java2023.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskMappingTest extends TaskOperations {
    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    TaskMapper taskMapper;

    @Test
    public void DtoToEntityTest() throws JsonProcessingException {
        Task task = genTaskWithoutDateTime();
        TaskDto taskDto = mapToDto(task);

        Task actualTask = taskMapper.toEntity(taskDto);
        verify(modelMapper).map(taskDto, Task.class);

        Assertions.assertEquals(task.hashCode(), actualTask.hashCode());
    }

    @Test
    public void EntityToDtoTest() throws JsonProcessingException {
        Task task = genTask();
        TaskViewDto taskDto = mapToViewDto(task);

        TaskViewDto actualTaskDto = taskMapper.toDto(task);
        verify(modelMapper).map(task, TaskViewDto.class);

        Assertions.assertEquals(taskDto.hashCode(), actualTaskDto.hashCode());
    }

    @Test
    public void ListEntityToListDtoTest() throws JsonProcessingException {
        List<Task> tasks = new ArrayList<>();
        for(int i=0; i<5; i++) {
            tasks.add(genTask());
        }

        List<TaskViewDto> tasksDto = new ArrayList<>();
        for (Task task : tasks) {
            tasksDto.add(mapToViewDto(task));
        }

        List<TaskViewDto> actualtasksDto = taskMapper.toListDto(tasks);
        verify(modelMapper).map(tasks, new TypeToken<List<TaskViewDto>>() {
        }.getType());

        Assertions.assertEquals(tasksDto.hashCode(), actualtasksDto.hashCode());
    }
}
