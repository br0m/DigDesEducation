package com.digdes.java2023.dto.team;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AllTeamMembersDto {
    private List<Map<String, String>> memberAndRoleList;

    public void add(Map<String, String> memberAndRole)
    {
        memberAndRoleList.add(memberAndRole);
    }
}
