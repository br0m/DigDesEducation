package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;

public class ChangeProjectStatusDto {

    private String codename;
    private ProjectStatus newStatus;

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public ProjectStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(ProjectStatus newStatus) {
        this.newStatus = newStatus;
    }
}
