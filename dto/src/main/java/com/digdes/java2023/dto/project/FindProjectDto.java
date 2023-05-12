package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;

import java.util.List;

public class FindProjectDto {

    private String findText;
    private List<ProjectStatus> projectStatusList;

    public String getFindText() {
        return findText;
    }

    public void setFindText(String findText) {
        this.findText = findText;
    }

    public List<ProjectStatus> getProjectStatusList() {
        return projectStatusList;
    }

    public void setProjectStatusList(List<ProjectStatus> projectStatusList) {
        this.projectStatusList = projectStatusList;
    }
}
