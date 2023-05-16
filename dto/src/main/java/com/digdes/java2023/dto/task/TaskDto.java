package com.digdes.java2023.dto.task;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.dto.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    private long id;
    private String title;
    private String description;
    private MemberDto responsibleMember;
    private long hoursCost;     //Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)
    private TaskStatus status;
    private MemberDto author;
}
