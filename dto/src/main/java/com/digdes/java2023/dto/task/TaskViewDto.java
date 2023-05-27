package com.digdes.java2023.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskViewDto extends TaskDto{

    private LocalDateTime deadline;
    private LocalDateTime creationDate;
    private LocalDateTime lastChangeDate;
}
