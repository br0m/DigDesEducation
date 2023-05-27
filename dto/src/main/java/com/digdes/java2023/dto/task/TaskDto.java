package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    private Integer id;
    @NotBlank
    private String title;
    private String description;
    private TeamMemberDto responsibleMember;
    @Min(value=1)
    @NotNull
    private Integer hoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private TaskStatus status;
    private TeamMemberDto author;
    @NotNull
    private ProjectDto project;
}
