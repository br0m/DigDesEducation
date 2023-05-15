package com.digdes.java2023.dto.task;

import java.util.List;

public class FindTaskResultDto {
    private List<TaskDto> taskDtoList;

    public void add(TaskDto taskDto)
    {
        taskDtoList.add(taskDto);
    }
    public List<TaskDto> getTaskDtoList() {
        return taskDtoList;
    }
    public void setTaskDtoList(List<TaskDto> taskDtoList) {
        this.taskDtoList = taskDtoList;
    }
}
