package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class  TeamMemberDto {

    private Integer id;
    @NotNull
    private ProjectDto project;
    @NotNull
    private MemberDto member;
    @NotNull
    private MemberRole role;
}
