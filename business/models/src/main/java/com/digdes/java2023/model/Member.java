package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String jobTitle;
    private String account;
    private String email;
    private MemberStatus status;
}
