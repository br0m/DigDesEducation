package com.digdes.java2023.model;

import com.digdes.java2023.model.enums.TaskStatus;

import java.time.LocalDateTime;

public class Task {
    private long id;
    private String title;
    private String description;
    private Member responsibleMember;
    private long hoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private LocalDateTime deadline;
    private TaskStatus status;
    private Member author;
    private LocalDateTime creationDate;
    private LocalDateTime lastChangeDate;

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

    public Member getResponsibleMember() {
        return responsibleMember;
    }

    public void setResponsibleMember(Member responsibleMember) {
        this.responsibleMember = responsibleMember;
    }

    public long getHoursCost() {
        return hoursCost;
    }

    public void setHoursCost(long hoursCost) {
        this.hoursCost = hoursCost;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(LocalDateTime lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
