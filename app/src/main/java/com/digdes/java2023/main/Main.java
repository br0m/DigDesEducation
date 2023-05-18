package com.digdes.java2023.main;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepository;
import com.digdes.java2023.repositories.impl.MemberRepositoryImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        MemberRepository memberRepository = new MemberRepositoryImpl();

        Member member = Member.builder().firstName("Иван").lastName("Иванов").patronymic("Иванович").account("ivanov").jobTitle("Developer").email("ivanov@mail.ru").status(MemberStatus.ACTIVE).build();
        member=memberRepository.createMember(member);

        Member member2 = Member.builder().id(member.getId()).firstName(member.getFirstName()).lastName(member.getLastName()).patronymic(member.getPatronymic()).jobTitle("Tester").status(member.getStatus()).build();
        member2=memberRepository.updateMember(member2);

        List<Member> list = memberRepository.findMember(member.getFirstName());

        Member member3 = memberRepository.getById(member2.getId());
        Member member4 = memberRepository.deleteById(member2.getId());

    }
}
