package com.digdes.java2023.repositories.impl;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepository {

    private static List<Member> members = new ArrayList<>();
    private static long id;
    private static final String fileName = "memberStorage.json";

    public MemberRepositoryImpl() {
        members = getAll();

        if (members.size() != 0)
            id = members.get(members.size() - 1).getId() + 1;
        else
            id = 0;
    }

    @Override
    public Member createMember(Member member) {
        member.setId(id++);
        members.add(member);
        writeMembers(members);

        return member;
    }

    @Override
    public Member updateMember(Member member) {
        for (Member oldMember : members) {
            if (oldMember.getId() == member.getId()) {

                if (oldMember.getStatus() == MemberStatus.REMOVED)
                    return null;

                if (member.getAccount() != null)
                    oldMember.setAccount(member.getAccount());
                if (member.getEmail() != null)
                    oldMember.setEmail(member.getEmail());
                if (member.getStatus() != null)
                    oldMember.setStatus(member.getStatus());
                if (member.getFirstName() != null)
                    oldMember.setFirstName(member.getFirstName());
                if (member.getLastName() != null)
                    oldMember.setLastName(member.getLastName());
                if (member.getPatronymic() != null)
                    oldMember.setPatronymic(member.getPatronymic());
                if (member.getJobTitle() != null)
                    oldMember.setJobTitle(member.getJobTitle());

                writeMembers(members);
                return oldMember;
            }
        }

        return null;
    }

    @Override
    public List<Member> findMember(String findText) {
        List<Member> findMembers = new ArrayList<>();

        for (Member member : members) {
            if ((member.getFirstName().contains(findText) || member.getLastName().contains(findText) || member.getPatronymic().contains(findText) || member.getAccount().contains(findText) || member.getEmail().contains(findText)) && member.getStatus() == MemberStatus.ACTIVE) {
                findMembers.add(member);
            }
        }
        return findMembers;
    }

    @Override
    public Member getById(long id) {
        for (Member member : members) {
            if (member.getId() == id)
                return member;
        }
        return null;
    }

    @Override
    public List<Member> getAll() {
        if (members.size()==0) {
            String membersJson;
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                membersJson = new String(Files.readAllBytes(Path.of(fileName)));
                if (!membersJson.isEmpty())
                    members = objectMapper.readValue(membersJson, new TypeReference<>() {
                    });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return members;
    }

    @Override
    public Member deleteById(long id) {
        for (Member member : members) {
            if (member.getId() == id) {
                member.setStatus(MemberStatus.REMOVED);
                writeMembers(members);
                return member;
            }
        }
        return null;
    }

    private void writeMembers(List<Member> members) {
        String membersJson;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            membersJson = objectMapper.writeValueAsString(members);
            Files.writeString(Path.of(fileName), membersJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
