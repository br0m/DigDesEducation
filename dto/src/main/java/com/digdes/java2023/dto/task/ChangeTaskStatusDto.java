package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;

public class ChangeTaskStatusDto {

    private long id;
    private TaskStatus newStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TaskStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }
}
