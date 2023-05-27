package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {

    private Integer id;
    @NotBlank
    private String codename;
    @NotBlank
    private String title;
    private String description;
    private ProjectStatus status;
}
