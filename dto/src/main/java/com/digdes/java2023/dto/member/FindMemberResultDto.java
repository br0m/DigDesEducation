package com.digdes.java2023.dto.member;

import java.util.List;

public class FindMemberResultDto {

    private List<MemberDto> memberDtoList;

    public void add(MemberDto memberDto) {
        memberDtoList.add(memberDto);
    }
}
