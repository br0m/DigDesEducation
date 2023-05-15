package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTaskStatusDto {

    private long id;
    private TaskStatus newStatus;
}
