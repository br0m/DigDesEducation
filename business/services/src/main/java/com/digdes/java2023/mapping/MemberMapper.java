package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.model.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberMapper {
    private final ModelMapper modelMapper;

    public Member toEntity(MemberDto memberDto) {
        return modelMapper.map(memberDto, Member.class);
    }

    public MemberDto toDto(Member member) {
        return modelMapper.map(member, MemberDto.class);
    }

    public List<MemberDto> toListDto(List<Member> members) {
        return modelMapper.map(members, new TypeToken<List<MemberDto>>() {
        }.getType());
    }
}
