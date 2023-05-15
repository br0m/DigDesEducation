package com.digdes.java2023.dto.team;

public class TeamDto {

    private long id;
    private String TitleAndCodename;
    private String memberAndRole;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitleAndCodename() {
        return TitleAndCodename;
    }
    public void setTitleAndCodename(String titleAndCodename) {
        TitleAndCodename = titleAndCodename;
    }
    public String getMemberAndRole() {
        return memberAndRole;
    }
    public void setMemberAndRole(String memberAndRole) {
        this.memberAndRole = memberAndRole;
    }
}
