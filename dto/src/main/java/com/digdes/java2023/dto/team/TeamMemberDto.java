package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO участника команды проекта")
public class  TeamMemberDto {

    @Schema(description = "Уникальный ID участника")
    private Integer id;

    @Schema(description = "DTO проекта")
    @NotNull
    private ProjectDto project;

    @Schema(description = "DTO сотрудника")
    @NotNull
    private MemberDto member;

    @Schema(description = "Роль сотрудника: TEAMLEAD, ANALYST, DEVELOPER, QA")
    @NotNull
    private MemberRole role;
}
