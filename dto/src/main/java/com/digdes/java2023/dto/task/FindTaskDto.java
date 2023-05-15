package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FindTaskDto {

    private long id;
    private String title;
    private String description;
    private String responsibleMember;
    private LocalDateTime deadline;
    private List<TaskStatus> taskStatusList;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime lastChangeDate;
    private TaskTimeFindParam creationPeriod;
    private TaskTimeFindParam deadlinePeriod;
}
