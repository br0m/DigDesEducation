package com.digdes.java2023.repositories;

import com.digdes.java2023.model.Member;

import java.util.List;

public interface MemberRepository {

    Member createMember(Member member);

    Member updateMember(Member member);

    List<Member> findMember(String findText);

    Member getById(long id);

    List<Member> getAll();

    Member deleteById(long id);

}
