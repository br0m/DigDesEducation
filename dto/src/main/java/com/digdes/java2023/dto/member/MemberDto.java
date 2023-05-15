package com.digdes.java2023.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private long id;
    private String displayedName;
    private String jobTitle;
    private String account;
    private String email;
    private String status;
}
