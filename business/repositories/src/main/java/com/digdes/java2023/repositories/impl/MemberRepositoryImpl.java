package com.digdes.java2023.repositories.impl;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberRepositoryImpl implements MemberRepository {

    private static long id;
    private static final File file = new File("memberStorage.json");

    public MemberRepositoryImpl() {
        List<Member> members = new ArrayList<>(getAll());

        if (members.size() != 0)
            id = members.get(members.size() - 1).getId() + 1;
        else
            id = 0;
    }

    @Override
    public Member createMember(Member member) {
        String memberJson;
        ObjectMapper objectMapper = new ObjectMapper();
        member.setId(id++);

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            memberJson = objectMapper.writeValueAsString(member);
            if (member.getId() != 0)
                memberJson = "\n" + memberJson;

            fileWriter.write(memberJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return member;
    }

    @Override
    public Member updateMember(Member member) {
        List<Member> members = new ArrayList<>(getAll());

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
        List<Member> members = new ArrayList<>(getAll());
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
        List<Member> members = new ArrayList<>(getAll());
        for (Member member : members) {
            if (member.getId() == id)
                return member;
        }
        return null;
    }

    @Override
    public List<Member> getAll() {
        String memberJson;
        List<Member> members = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                memberJson = scanner.nextLine();
                members.add(objectMapper.readValue(memberJson, Member.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return members;
    }

    @Override
    public Member deleteById(long id) {
        List<Member> members = new ArrayList<>(getAll());
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                members.remove(i);
                writeMembers(members);
                return members.get(i);
            }
        }
        return null;
    }

    private void writeMembers(List<Member> members) {
        String memberJson;
        ObjectMapper objectMapper = new ObjectMapper();

        try (FileWriter fileWriter = new FileWriter(file, false)) {
            for (Member member : members) {
                memberJson = objectMapper.writeValueAsString(member);
                if (member.getId() != 0)
                    memberJson = "\n" + memberJson;

                fileWriter.write(memberJson);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
