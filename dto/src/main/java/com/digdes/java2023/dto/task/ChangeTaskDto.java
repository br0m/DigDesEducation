package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeTaskDto {

    private long id;
    private String newTitle;
    private String newDescription;
    private MemberDto newResponsibleMember;
    private long newHoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private MemberDto newAuthor;
}
