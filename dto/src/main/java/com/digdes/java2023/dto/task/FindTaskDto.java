package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;
import com.digdes.java2023.dto.team.TeamMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "DTO для поиска задач")
public class FindTaskDto {

    @Schema(description = "Наименование")
    private String title;

    @Schema(description = "Ответственный сотрудник")
    private TeamMemberDto responsibleMember;

    @Schema(description = "Статус: NEW, WORKING, FINISHED, CLOSED")
    private TaskStatus status;

    @Schema(description = "Автор задачи")
    private TeamMemberDto author;

    @Schema(description = "Крайний срок исполнения")
    private LocalDateTime deadline = LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC);

    @Schema(description = "Дата создания")
    private LocalDateTime creationDate = LocalDateTime.ofEpochSecond(0,0, ZoneOffset.UTC);

    @Schema(description = "Критерий поиска по сроку исполнения: BEFORE, AFTER")
    private TaskTimeFindParam deadlinePeriod = TaskTimeFindParam.AFTER;

    @Schema(description = "Критерий поиска по дате создания: BEFORE, AFTER")
    private TaskTimeFindParam creationPeriod = TaskTimeFindParam.AFTER;
}
