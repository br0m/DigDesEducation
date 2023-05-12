package com.digdes.java2023.dto.team;

public class TeamDto {

    private long id;
    private String TitleAndCodename;
    private String memberAndRole;

    public void setId(long id) {
        this.id = id;
    }

    public void setTitleAndCodename(String titleAndCodename) {
        TitleAndCodename = titleAndCodename;
    }

    public void setMemberAndRole(String memberAndRole) {
        this.memberAndRole = memberAndRole;
    }
}
