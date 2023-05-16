package com.digdes.java2023.dto.task;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
public class FindTaskResultDto {
    private List<TaskViewDto> taskDtoList;

    public void add(TaskViewDto taskDto)
    {
        taskDtoList.add(taskDto);
    }
}
