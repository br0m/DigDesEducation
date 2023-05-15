package com.digdes.java2023.dto.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProjectDto {

    private String codename;
    private String newCodename;
    private String newTitle;
    private String newDescription;
}
