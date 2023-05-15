package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
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
}
