package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FindTaskDto extends TaskViewDto{

    private List<TaskStatus> taskStatusList;
    private TaskTimeFindParam creationPeriod;
    private TaskTimeFindParam deadlinePeriod;
}
