package com.digdes.java2023.dto.task;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class FindTaskResultDto {
    private List<TaskDto> taskDtoList;

    public void add(TaskDto taskDto)
    {
        taskDtoList.add(taskDto);
    }
}
