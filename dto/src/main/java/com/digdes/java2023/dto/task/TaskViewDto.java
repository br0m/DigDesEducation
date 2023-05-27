package com.digdes.java2023.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "DTO задачи с генерируемыми полями даты/времени")
public class TaskViewDto extends TaskDto{

    @Schema(description = "Крайний срок исполнения")
    private LocalDateTime deadline;

    @Schema(description = "Дата создания")
    private LocalDateTime creationDate;

    @Schema(description = "Дата последнего изменения")
    private LocalDateTime lastChangeDate;
}
