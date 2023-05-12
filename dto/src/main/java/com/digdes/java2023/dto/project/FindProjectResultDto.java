package com.digdes.java2023.dto.project;

import java.util.List;

public class FindProjectResultDto {

    private List<ProjectDto> projectDtoList;

    public void add(ProjectDto projectDto) {
        projectDtoList.add(projectDto);
    }
}
