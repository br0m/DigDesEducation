package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
