package com.digdes.java2023.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    private long id;
    private String title;
    private String description;
    private String responsibleMember;
    private String deadline;
    private String status;
    private String author;
    private String creationDate;
    private String lastChangeDate;
}
