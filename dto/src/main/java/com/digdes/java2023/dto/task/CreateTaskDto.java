package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.member.MemberDto;

public class CreateTaskDto {

    private String title;
    private String description;
    private MemberDto responsibleMember;
    private long hoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private TaskStatus status;
    private MemberDto author;

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

    public MemberDto getResponsibleMember() {
        return responsibleMember;
    }

    public void setResponsibleMember(MemberDto responsibleMember) {
        this.responsibleMember = responsibleMember;
    }

    public long getHoursCost() {
        return hoursCost;
    }

    public void setHoursCost(long hoursCost) {
        this.hoursCost = hoursCost;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public MemberDto getAuthor() {
        return author;
    }

    public void setAuthor(MemberDto author) {
        this.author = author;
    }
}
