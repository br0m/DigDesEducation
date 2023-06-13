package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.model.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;

import java.util.Random;

import static org.instancio.Select.field;

public class MemberOperations {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected static final CreateMemberDto blankFirstNameMember = Instancio.of(CreateMemberDto.class).set(field(CreateMemberDto::getFirstName), " ").create();
    protected static final CreateMemberDto blankLastNameMember = Instancio.of(CreateMemberDto.class).set(field(CreateMemberDto::getLastName), " ").create();
    protected static final CreateMemberDto blankAccountMember = Instancio.of(CreateMemberDto.class).set(field(CreateMemberDto::getAccount), " ").create();
    protected static final CreateMemberDto blankPasswordMember = Instancio.of(CreateMemberDto.class).set(field(CreateMemberDto::getPassword), " ").create();
    protected Member member;
    protected Member genMember() {
        return Instancio.create(Member.class);
    }

    protected MemberDto mapToDto(Member member) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(member), MemberDto.class);
    }

    protected Integer genRandomId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected Member genActiveMember(Integer id) {
        return Instancio.of(Member.class)
                .set(field(Member::getId), id)
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .create();
    }

    protected Member genRemovedMember(Integer id) {
        return Instancio.of(Member.class)
                .set(field(Member::getId), id)
                .set(field(Member::getStatus), MemberStatus.REMOVED)
                .create();
    }
}
