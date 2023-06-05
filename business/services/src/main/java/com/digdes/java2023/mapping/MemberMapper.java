package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CommonsLog
public class MemberMapper {
    private final ModelMapper modelMapper;

    public Member toEntity(MemberDto memberDto) {
        log.debug("Map DTO to entity");
        return modelMapper.map(memberDto, Member.class);
    }

    public MemberDto toDto(Member member) {
        log.debug("Map entity to DTO");
        return modelMapper.map(member, MemberDto.class);
    }

    public List<MemberDto> toListDto(List<Member> members) {
        log.debug("Map entity list to DTO list");
        return modelMapper.map(members, new TypeToken<List<MemberDto>>() {
        }.getType());
    }
}
