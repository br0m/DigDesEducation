package com.digdes.java2023.dto.team;

public class RemoveTeamMemberDto {

    private String codename;
    private long memberId;

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
}
