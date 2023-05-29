package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.mapping.MemberMapper;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.MemberDetails;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.services.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepositoryJpa memberRepositoryJpa;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper mapper;

    @Override
    public MemberDto getById(@NotNull Integer id) {
        return mapper.toDto(getEntityById(id));
    }

    @Override
    public MemberDto create(@Valid CreateMemberDto memberDto) {
        Member member = mapper.toEntity(memberDto);
        if (memberRepositoryJpa.findByAccountAndStatus(member.getAccount(), MemberStatus.ACTIVE).isPresent())
            throw new PropertyValueException("Not unique value", "member", "account");
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setStatus(MemberStatus.ACTIVE);
        memberRepositoryJpa.save(member);
        return mapper.toDto(member);
    }

    @Override
    public MemberDto update(@NotNull Integer id, @Valid CreateMemberDto memberDto) {
        Member oldMember = getEntityById(id);
        if (oldMember.getStatus() == MemberStatus.REMOVED)
            throw new PropertyValueException("Member already removed", "member", "id");
        Member member = mapper.toEntity(memberDto);
        member.setId(id);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        if (member.getStatus() == null)
            member.setStatus(oldMember.getStatus());
        memberRepositoryJpa.save(member);
        return mapper.toDto(member);
    }

    @Override
    public MemberDto remove(@NotNull Integer id) {
        Member member = getEntityById(id);
        if (member.getStatus() == MemberStatus.REMOVED)
            throw new PropertyValueException("Member already removed", "member", "id");
        int count = memberRepositoryJpa.removeById(id);
        member.setStatus(MemberStatus.REMOVED);
        return count == 1 ? mapper.toDto(member) : null;

    }

    @Override
    public List<MemberDto> find(@Min(3) String text) {
        List<Member> members = memberRepositoryJpa.findAllByTextAndStatus(text, MemberStatus.ACTIVE);
        return mapper.toListDto(members);
    }

    private Member getEntityById(Integer id) {
        return memberRepositoryJpa.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "member"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepositoryJpa.findByAccount(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new MemberDetails(member);
    }

    @PostConstruct
    public void initAdmin() {
        Optional<Member> member = memberRepositoryJpa.findByAccount("admin");
        if (member.isEmpty())
            memberRepositoryJpa.save(Member.builder().lastName("admin").firstName("admin").status(MemberStatus.ACTIVE).account("admin").password(passwordEncoder.encode("admin")).build());
    }
}
