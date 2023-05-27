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
import java.util.Optional;

public class MemberRepositoryFileImpl implements MemberRepository {

    private static List<Member> members;
    private static int id;
    private static final String fileName = "memberStorage.json";

    public MemberRepositoryFileImpl() {
        members = getAll();
        if(members!=null)
            id = members.size();
    }

    @Override
    public Member createMember(Member member) {
        if(members==null)
            return null;

        member.setId(id++);
        members.add(member);
        writeMembers(members);
        return member;
    }

    @Override
    public Member updateMember(Member member) {
        if(members==null)
            return null;

        for (Member oldMember : members) {
            if (oldMember.getId() == member.getId()) {

                if (oldMember.getStatus() == MemberStatus.REMOVED)
                    return null;

                long id = oldMember.getId();
                members.remove((int) id);
                members.add((int) id, member);
                writeMembers(members);
                return member;
            }
        }

        return null;
    }

    @Override
    public List<Member> findMember(String findText) {
        if(members==null)
            return null;

        List<Member> findMembers = new ArrayList<>();

        for (Member member : members) {
            if ((isContains(member.getLastName(), findText) || isContains(member.getFirstName(), findText) || isContains(member.getPatronymic(), findText) || isContains(member.getAccount(), findText) || isContains(member.getEmail(), findText)) && member.getStatus() == MemberStatus.ACTIVE) {
                findMembers.add(member);
            }
        }
        return findMembers;
    }

    private boolean isContains(String param, String text)
    {
        return param!=null && param.contains(text);
    }

    @Override
    public Member getById(long id) {
        if(members==null)
            return null;

        Optional<Member> memberById = members.stream().filter(member -> member.getId()==id).findFirst();
        return memberById.orElse(null);
    }

    @Override
    public List<Member> getAll() {
        if (members==null) {
            String membersJson;
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                membersJson = new String(Files.readAllBytes(Path.of(fileName)));
                members=new ArrayList<>();
                if (!membersJson.isEmpty())
                    members = objectMapper.readValue(membersJson, new TypeReference<>() {
                    });
            } catch (IOException ex) {
                System.out.println("Can't read members from " + fileName);
                return null;
            }
        }

        return members;
    }

    @Override
    public Member deleteById(long id) {
        if(members==null)
            return null;

        Member member=getById(id);
        if(member!=null && member.getStatus()!=MemberStatus.REMOVED)
        {
            member.setStatus(MemberStatus.REMOVED);
            writeMembers(members);
            return member;
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
