package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTeamMemberDto {

    private String codename;
    private long memberId;
    private MemberRole memberRole;
}
