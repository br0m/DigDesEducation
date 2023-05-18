package com.digdes.java2023.dto.project;

import com.digdes.java2023.dto.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FindProjectDto {

    private String findText;
    private List<ProjectStatus> projectStatusList;
}
