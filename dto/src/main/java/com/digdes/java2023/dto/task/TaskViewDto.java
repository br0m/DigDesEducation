package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.member.MemberDto;
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
