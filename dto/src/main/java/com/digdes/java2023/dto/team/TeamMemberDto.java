package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberDto {

    private ProjectDto projectDto;
    private MemberDto memberDto;
    private MemberRole memberRole;
}
