package com.digdes.java2023.dto.member;

import com.digdes.java2023.dto.enums.MemberStatus;

public class FindMemberDto {

    private String findText;

    public String getFindText() {
        return findText;
    }

    public void setFindText(String findText) {
        this.findText = findText;
    }
}
