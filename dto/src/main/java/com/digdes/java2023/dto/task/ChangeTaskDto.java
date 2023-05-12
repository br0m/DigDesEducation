package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.member.MemberDto;

import java.time.LocalDateTime;

public class ChangeTaskDto {

    private long id;
    private String newTitle;
    private String newDescription;
    private MemberDto newResponsibleMember;
    private long newHoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private MemberDto newAuthor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public MemberDto getNewResponsibleMember() {
        return newResponsibleMember;
    }

    public void setNewResponsibleMember(MemberDto newResponsibleMember) {
        this.newResponsibleMember = newResponsibleMember;
    }

    public long getNewHoursCost() {
        return newHoursCost;
    }

    public void setNewHoursCost(long newHoursCost) {
        this.newHoursCost = newHoursCost;
    }

    public MemberDto getNewAuthor() {
        return newAuthor;
    }

    public void setNewAuthor(MemberDto newAuthor) {
        this.newAuthor = newAuthor;
    }

}
