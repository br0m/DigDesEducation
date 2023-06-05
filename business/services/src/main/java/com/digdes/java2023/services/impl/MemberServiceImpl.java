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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
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
@CommonsLog
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
        log.info("Creating new member");
        log.debug(memberDto.toString());

        Member member = mapper.toEntity(memberDto);
        accountUniqueCheck(member.getAccount());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setStatus(MemberStatus.ACTIVE);
        member.setId(null);
        memberRepositoryJpa.save(member);

        log.info("New member successfully created");
        log.debug(member.toString());
        return mapper.toDto(member);
    }

    @Override
    public MemberDto update(@NotNull Integer id, @Valid CreateMemberDto memberDto) {
        log.info("Updating member with id = " + id);
        log.debug(memberDto.toString());

        Member oldMember = getEntityById(id);
        if (oldMember.getStatus() == MemberStatus.REMOVED) {
            log.warn("Member with id = " + id + " already removed");
            throw new PropertyValueException("Member already removed", "member", "id");
        }

        Member member = mapper.toEntity(memberDto);
        if (!member.getAccount().equals(oldMember.getAccount()))
            accountUniqueCheck(member.getAccount());
        member.setId(id);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        if (member.getStatus() == null)
            member.setStatus(oldMember.getStatus());
        memberRepositoryJpa.save(member);

        log.info("Member with id = " + id + " successfully updated");
        log.debug(member.toString());
        return mapper.toDto(member);
    }

    @Override
    public MemberDto remove(@NotNull Integer id) {
        log.info("Removing member with id = " + id);

        Member member = getEntityById(id);
        if (member.getStatus() == MemberStatus.REMOVED) {
            log.warn("Member with id = " + id + " already removed");
            throw new PropertyValueException("Member already removed", "member", "id");
        }

        int count = memberRepositoryJpa.removeById(id);
        member.setStatus(MemberStatus.REMOVED);

        if(count==1) {
            log.info("Member with id = " + id + " successfully removed");
            return mapper.toDto(member);
        }
        log.warn("Remove error member with id = " + id);
        return null;
    }

    @Override
    public List<MemberDto> find(@Size(min = 3) String text) {
        log.info("Searching member with text = " + text);
        List<Member> members = memberRepositoryJpa.findAllByTextAndStatus(text, MemberStatus.ACTIVE);
        log.info("Search completed");
        log.debug(members.toString());
        return mapper.toListDto(members);
    }

    private Member getEntityById(Integer id) {
        log.info("Searching member with id = " + id);

        Member member = memberRepositoryJpa.findById(id).orElse(null);
        if (member == null) {
            log.warn("Member with id = " + id + " not found");
            throw new ObjectNotFoundException(id, "member");
        }

        log.info("Member with id = " + id + " successfully found");
        log.debug(member.toString());
        return member;
    }

    private void accountUniqueCheck(String account) {
        if (memberRepositoryJpa.findByAccountAndStatus(account, MemberStatus.ACTIVE).isPresent()) {
            log.warn("Account " + account + " already exist");
            throw new PropertyValueException("Not unique value", "member", "account");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepositoryJpa.findByAccount(username).orElse(null);
        if (member == null) {
            String message = "Member " + username + " not found";
            log.warn(message);
            throw new UsernameNotFoundException(message);
        }
        log.info("Member " + username + " successfully authenticated");
        return new MemberDetails(member);
    }

    @PostConstruct
    public void initAdmin() {
        Optional<Member> member = memberRepositoryJpa.findByAccount("admin");
        if (member.isEmpty()) {
            log.warn("Member admin not found");
            memberRepositoryJpa.save(Member.builder().lastName("admin").firstName("admin").status(MemberStatus.ACTIVE).account("admin").password(passwordEncoder.encode("admin")).build());
            log.info("Member admin successfully created");
        }
    }
}
