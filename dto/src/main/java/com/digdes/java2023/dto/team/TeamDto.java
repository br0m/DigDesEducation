package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class TeamDto {

    private ProjectDto project;
    private Map<MemberDto, MemberRole> roles;
}
