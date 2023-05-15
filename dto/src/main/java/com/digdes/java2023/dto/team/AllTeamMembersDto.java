package com.digdes.java2023.dto.team;

import java.util.List;
import java.util.Map;

public class AllTeamMembersDto {
    private List<Map<String, String>> memberAndRoleList;

    public void add(Map<String, String> memberAndRole)
    {
        memberAndRoleList.add(memberAndRole);
    }
    public List<Map<String, String>> getMemberAndRoleList() {
        return memberAndRoleList;
    }
    public void setMemberAndRoleList(List<Map<String, String>> memberAndRoleList) {
        this.memberAndRoleList = memberAndRoleList;
    }
}
