package com.digdes.java2023.dto.member;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FindMemberResultDto {

    private List<MemberDto> memberDtoList;

    public void add(MemberDto memberDto) {
        memberDtoList.add(memberDto);
    }
}
