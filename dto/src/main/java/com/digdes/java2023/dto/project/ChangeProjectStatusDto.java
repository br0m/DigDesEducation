package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProjectStatusDto {

    private String codename;
    private ProjectStatus newStatus;
}
