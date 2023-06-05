package com.digdes.java2023;

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

    protected Member genMember() {
        return Instancio.create(Member.class);
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

    protected Member genNullStatusMember(Integer id) {
        return Instancio.of(Member.class)
                .set(field(Member::getId), id)
                .ignore(field(Member::getStatus))
                .create();
    }

    protected Member copyMember(Member member) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(member), Member.class);
    }

    protected MemberDto mapToDto(Member member) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(member), MemberDto.class);
    }

    protected CreateMemberDto mapToCreateDto(Member member) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(member), CreateMemberDto.class);
    }

    protected Integer genRandomId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }
}
