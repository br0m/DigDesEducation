package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.enums.TaskTimeFindParam;


import java.time.LocalDateTime;
import java.util.List;

public class FindTaskDto {

    private long id;
    private String title;
    private String description;
    private String responsibleMember;
    private LocalDateTime deadline;
    private List<TaskStatus> taskStatusList;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime lastChangeDate;
    private TaskTimeFindParam creationPeriod;
    private TaskTimeFindParam deadlinePeriod;

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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<TaskStatus> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(List<TaskStatus> taskStatusList) {
        this.taskStatusList = taskStatusList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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

    public TaskTimeFindParam getCreationPeriod() {
        return creationPeriod;
    }

    public void setCreationPeriod(TaskTimeFindParam creationPeriod) {
        this.creationPeriod = creationPeriod;
    }

    public TaskTimeFindParam getDeadlinePeriod() {
        return deadlinePeriod;
    }

    public void setDeadlinePeriod(TaskTimeFindParam deadlinePeriod) {
        this.deadlinePeriod = deadlinePeriod;
    }
}
