package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.enums.MemberRole;

public class AddTeamMemberDto {

    private String codename;
    private long memberId;
    private MemberRole memberRole;

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
