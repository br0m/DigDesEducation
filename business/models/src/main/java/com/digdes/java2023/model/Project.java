package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
    private long id;
    private String codename;
    private String title;
    private String description;
    private ProjectStatus status;
}
