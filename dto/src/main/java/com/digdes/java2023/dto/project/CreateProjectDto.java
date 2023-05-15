package com.digdes.java2023.dto.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectDto {

    private String codename;
    private String title;
    private String description;
}
