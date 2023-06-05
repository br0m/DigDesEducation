package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.config.ModelConfig;
import com.digdes.java2023.repositories.config.RepositoryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {RepositoryConfig.class, ModelConfig.class, MemberRepositoryJpa.class})
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryJpaTest extends MemberOperations {
    @Autowired
    private MemberRepositoryJpa memberRepository;

    @Test
    public void createTest() throws IOException {
        Member member = genMember();
        Member cloneMember = copyMember(member);
        memberRepository.save(member);

        Assertions.assertNotEquals(null, member.getId());
        cloneMember.setId(member.getId());
        Assertions.assertEquals(cloneMember.hashCode(), member.hashCode());
    }

    @Test
    public void updateTest() throws IOException {
        Member member = genMember();
        memberRepository.save(member);

        Member updateMember = genMember();
        updateMember.setId(member.getId());
        Member cloneUpdateMember = copyMember(updateMember);
        memberRepository.save(updateMember);

        Assertions.assertEquals(cloneUpdateMember.hashCode(), updateMember.hashCode());
    }

    @Test
    public void findByIdTest() {
        Member member = genMember();
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(member.getId());
        assert findMember.isPresent();
        Assertions.assertEquals(member.hashCode(), findMember.get().hashCode());
    }

    @Test
    public void removeTest() {
        Member member = genActiveMember();
        memberRepository.save(member);
        int count = memberRepository.removeById(member.getId());
        Optional<Member> removedMember = memberRepository.findById(member.getId());

        assert removedMember.isPresent();
        member.setStatus(removedMember.get().getStatus());

        Assertions.assertEquals(1, count);
        Assertions.assertEquals(member.hashCode(), removedMember.get().hashCode());
        Assertions.assertEquals(MemberStatus.REMOVED, removedMember.get().getStatus());
    }

    @Test
    public void findByAccountTest() {
        Member member = genMember();
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByAccount(member.getAccount());
        assert findMember.isPresent();
        Assertions.assertEquals(member.hashCode(), findMember.get().hashCode());
    }

    @Test
    public void findByAccountAndStatusTest() {
        Member member = genMember();
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findByAccountAndStatus(member.getAccount(), member.getStatus());
        assert findMember.isPresent();
        Assertions.assertEquals(member.hashCode(), findMember.get().hashCode());
    }


    @Test
    public void findAllByTextAndStatusTest() throws IOException {
        Member activeMember = genActiveMember();
        Member removedMember = genRemovedMember();

        List<Member> activeMembers = new ArrayList<>();
        List<Member> removedMembers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            activeMembers.add(copyMember(activeMember));
            removedMembers.add(copyMember(removedMember));
        }

        memberRepository.saveAll(activeMembers);
        memberRepository.saveAll(removedMembers);

        findMembers(activeMember.getFirstName(), activeMembers);
        findMembers(activeMember.getLastName(), activeMembers);
        findMembers(activeMember.getPatronymic(), activeMembers);
        findMembers(activeMember.getAccount(), activeMembers);
        findMembers(activeMember.getEmail(), activeMembers);
    }

    private void findMembers(String param, List<Member> activeMembers) {
        List<Member> findMembersList = memberRepository.findAllByTextAndStatus(param, MemberStatus.ACTIVE);
        Assertions.assertEquals(activeMembers.size(), findMembersList.size());
        Assertions.assertEquals(activeMembers.hashCode(), findMembersList.hashCode());
    }
}
