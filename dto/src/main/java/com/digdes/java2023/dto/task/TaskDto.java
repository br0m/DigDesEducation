package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "DTO задачи")
public class TaskDto {

    @Schema(description = "Уникальный ID задачи")
    private Integer id;

    @Schema(description = "Наименование")
    @NotBlank
    private String title;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Ответственный сотрудник")
    private TeamMemberDto responsibleMember;

    @Schema(description = "Трудозатраты в часах")
    @Min(value=1)
    @NotNull
    private Integer hoursCost;

    @Schema(description = "Статус: NEW, WORKING, FINISHED, CLOSED")
    private TaskStatus status;

    @Schema(description = "Автор задачи")
    private TeamMemberDto author;

    @Schema(description = "Проект к которому относится задача")
    @NotNull
    private ProjectDto project;
}
