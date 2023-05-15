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

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getResponsibleMember() {
        return responsibleMember;
    }
    public void setResponsibleMember(String responsibleMember) {
        this.responsibleMember = responsibleMember;
    }
    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public String getLastChangeDate() {
        return lastChangeDate;
    }
    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
