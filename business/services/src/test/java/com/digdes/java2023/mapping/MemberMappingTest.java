package com.digdes.java2023.mapping;

import com.digdes.java2023.MemberOperations;
import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.model.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberMappingTest extends MemberOperations {

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    MemberMapper memberMapper;

    @Test
    public void DtoToEntityTest() throws JsonProcessingException {
        Member member = genMember();
        CreateMemberDto memberDto = mapToCreateDto(member);

        Member actualMember = memberMapper.toEntity(memberDto);
        verify(modelMapper).map(memberDto, Member.class);

        Assertions.assertEquals(member.hashCode(), actualMember.hashCode());
    }

    @Test
    public void EntityToDtoTest() throws JsonProcessingException {
        Member member = genMember();
        MemberDto memberDto = mapToDto(member);

        MemberDto actualMemberDto = memberMapper.toDto(member);
        verify(modelMapper).map(member, MemberDto.class);

        Assertions.assertEquals(memberDto.hashCode(), actualMemberDto.hashCode());
    }

    @Test
    public void ListEntityToListDtoTest() throws JsonProcessingException {
        List<Member> members = Instancio.ofList(Member.class).size(5).create();
        List<MemberDto> membersDto = new ArrayList<>();

        for (Member member : members) {
            membersDto.add(mapToDto(member));
        }

        List<MemberDto> actualMembersDto = memberMapper.toListDto(members);
        verify(modelMapper).map(members, new TypeToken<List<MemberDto>>() {
        }.getType());

        for (int i=0; i<membersDto.size(); i++) {
            Assertions.assertEquals(membersDto.get(i).hashCode(), actualMembersDto.get(i).hashCode());
        }
    }
}
