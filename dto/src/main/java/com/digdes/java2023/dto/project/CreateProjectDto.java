package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;

public class CreateProjectDto {

    private String codename;
    private String title;
    private String description;

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
