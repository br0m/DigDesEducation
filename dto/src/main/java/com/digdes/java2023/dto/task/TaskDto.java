package com.digdes.java2023.dto.task;

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

    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setResponsibleMember(String responsibleMember) {
        this.responsibleMember = responsibleMember;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
