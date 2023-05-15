package com.digdes.java2023.dto.project;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FindProjectResultDto {

    private List<ProjectDto> projectDtoList;

    public void add(ProjectDto projectDto) {
        projectDtoList.add(projectDto);
    }
}
