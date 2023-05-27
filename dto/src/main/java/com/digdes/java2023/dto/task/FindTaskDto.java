package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import com.digdes.java2023.dto.team.TeamMemberDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
public class FindTaskDto {

    private String title;
    private TeamMemberDto responsibleMember;
    private TaskStatus status;
    private TeamMemberDto author;
    private LocalDateTime deadline = LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC);
    private LocalDateTime creationDate = LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC);
    private TaskTimeFindParam deadlinePeriod = TaskTimeFindParam.AFTER;
    private TaskTimeFindParam creationPeriod = TaskTimeFindParam.AFTER;
}
