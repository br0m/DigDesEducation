package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@Schema(description = "DTO команды проекта")
public class TeamDto {

    @Schema(description = "DTO проекта")
    private ProjectDto project;

    @Schema(description = "Map<участник_проекта, его роль>")
    private Map<MemberDto, MemberRole> roles;
}
