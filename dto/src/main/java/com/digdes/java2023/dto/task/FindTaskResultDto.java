package com.digdes.java2023.dto.task;

import java.util.List;

public class FindTaskResultDto {
    private List<TaskDto> taskDtoList;

    public void add(TaskDto taskDto)
    {
        taskDtoList.add(taskDto);
    }
}
