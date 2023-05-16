package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {
    private long id;
    private String codename;
    private String title;
    private String description;
    private ProjectStatus status;
}
