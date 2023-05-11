package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.MemberRole;

import java.util.Map;

public class Team {

    private long id;
    private Project project;
    private Map<Member, MemberRole> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Map<Member, MemberRole> getRoles() {
        return roles;
    }

    public void setRoles(Map<Member, MemberRole> roles) {
        this.roles = roles;
    }
}
