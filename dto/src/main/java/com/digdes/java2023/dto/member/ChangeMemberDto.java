package com.digdes.java2023.dto.member;

import com.digdes.java2023.dto.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMemberDto extends OperationByIdMemberDto {

    private String newFirstName;
    private String newLastName;
    private String newPatronymic;
    private String newJobTitle;
    private String newAccount;
    private String newEmail;
    private MemberStatus newStatus;
}
