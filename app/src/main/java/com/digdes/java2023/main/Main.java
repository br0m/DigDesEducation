package com.digdes.java2023.main;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepository;
import com.digdes.java2023.repositories.impl.MemberRepositoryImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        MemberRepository memberRepository = new MemberRepositoryImpl();

        Member member = new Member();
        member.setFirstName("Иван");
        member.setLastName("Иванов");
        member.setPatronymic("Иванович");
        member.setAccount("ivanov");
        member.setJobTitle("Developer");
        member.setEmail("ivanov@mail.ru");
        member.setStatus(MemberStatus.ACTIVE);
        member=memberRepository.createMember(member);

        Member member2 = new Member();
        member2.setId(0);
        member2.setJobTitle("Tester");
        member2=memberRepository.updateMember(member2);

        List<Member> list = memberRepository.findMember("Иван");

        Member member3 = memberRepository.getById(1);
        Member member4 = memberRepository.deleteById(0);

    }
}
