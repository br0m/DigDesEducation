package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;

import java.io.IOException;

import static org.instancio.Select.field;
@RequiredArgsConstructor
public class MemberOperations extends Init{
    private final ObjectMapper objectMapper;

    protected Member genMember() {
        return Instancio.create(Member.class);
    }

    protected Member genActiveMember() {
        return Instancio.of(Member.class)
                .ignore(field(Member::getId))
                .set(field(Member::getStatus), MemberStatus.ACTIVE)
                .create();
    }

    protected Member genRemovedMember() {
        return Instancio.of(Member.class)
                .ignore(field(Member::getId))
                .set(field(Member::getStatus), MemberStatus.REMOVED)
                .create();
    }

    protected Member copyMember(Member member) throws IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(member), Member.class);
    }
}
