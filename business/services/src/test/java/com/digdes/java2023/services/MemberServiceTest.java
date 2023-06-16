package com.digdes.java2023.services;

import com.digdes.java2023.MemberOperations;
import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.mapping.MemberMapper;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.MemberDetails;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.services.impl.MemberServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends MemberOperations {

    @Spy
    MemberRepositoryJpa memberRepositoryJpa;
    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    MemberMapper mapper;
    @InjectMocks
    MemberServiceImpl memberService;

    @Test
    public void createNotExistTest() throws JsonProcessingException {
        Member inputMember = genRemovedMember(genRandomId());

        Member outputMember = copyMember(inputMember);
        outputMember.setId(null);
        outputMember.setStatus(MemberStatus.ACTIVE);
        String hashPassword = passwordEncoder.encode(inputMember.getPassword());
        outputMember.setPassword(hashPassword);

        CreateMemberDto inputMemberDto = mapToCreateDto(inputMember);
        MemberDto outputMemberDto = mapToDto(outputMember);

        when(mapper.toEntity(inputMemberDto)).thenReturn(inputMember);
        when(memberRepositoryJpa.findByAccountAndStatus(inputMember.getAccount(), MemberStatus.ACTIVE)).thenReturn(Optional.empty());
        when(mapper.toDto(inputMember)).thenReturn(outputMemberDto);
        when(passwordEncoder.encode(inputMember.getPassword())).thenReturn(hashPassword);

        MemberDto actualOutputMemberDto = memberService.create(inputMemberDto);
        verify(memberRepositoryJpa).save(argThat(m -> m.hashCode() == outputMember.hashCode()));
        Assertions.assertEquals(outputMemberDto.hashCode(), actualOutputMemberDto.hashCode());
    }

    @Test
    public void createExistTest() throws JsonProcessingException {
        Member inputMember = genMember();
        CreateMemberDto inputMemberDto = mapToCreateDto(inputMember);

        when(mapper.toEntity(inputMemberDto)).thenReturn(inputMember);
        when(memberRepositoryJpa.findByAccountAndStatus(inputMember.getAccount(), MemberStatus.ACTIVE)).thenReturn(Optional.of(inputMember));

        verify(memberRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> memberService.create(inputMemberDto));
    }

    @Test
    public void getByIdExistTest() throws JsonProcessingException {
        Member member = genMember();
        MemberDto memberDto = mapToDto(member);

        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(mapper.toDto(member)).thenReturn(memberDto);

        MemberDto actualMemberDto = memberService.getById(member.getId());
        Assertions.assertEquals(memberDto.hashCode(), actualMemberDto.hashCode());
    }

    @Test
    public void getByIdNotExistTest() {
        Integer id = genRandomId();
        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> memberService.getById(id));
    }

    @Test
    public void updateExistTest() throws JsonProcessingException {
        Member inputMember = genActiveMember(null);
        Integer id = genRandomId();
        Member oldMember = genActiveMember(id);
        oldMember.setAccount(inputMember.getAccount());

        Member outputMember = copyMember(inputMember);
        outputMember.setId(id);
        String hashPassword = passwordEncoder.encode(inputMember.getPassword());
        outputMember.setPassword(hashPassword);

        CreateMemberDto inputMemberDto = mapToCreateDto(inputMember);
        MemberDto outputMemberDto = mapToDto(outputMember);

        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.of(oldMember));
        when(mapper.toEntity(inputMemberDto)).thenReturn(inputMember);
        when(mapper.toDto(inputMember)).thenReturn(outputMemberDto);
        when(passwordEncoder.encode(inputMember.getPassword())).thenReturn(hashPassword);

        MemberDto actualOutputMemberDto = memberService.update(id, inputMemberDto);
        verify(memberRepositoryJpa).save(argThat(m -> m.hashCode() == outputMember.hashCode()));
        Assertions.assertEquals(outputMemberDto.hashCode(), actualOutputMemberDto.hashCode());
    }

    @Test
    public void updateExistAccountNotUniqueTest() throws JsonProcessingException {
        Member inputMember = genActiveMember(null);
        Integer id = genRandomId();
        Member oldMember = genActiveMember(id);
        oldMember.setAccount(inputMember.getAccount() + "different");

        CreateMemberDto inputMemberDto = mapToCreateDto(inputMember);

        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.of(oldMember));
        when(mapper.toEntity(inputMemberDto)).thenReturn(inputMember);
        when(memberRepositoryJpa.findByAccountAndStatus(inputMember.getAccount(), MemberStatus.ACTIVE)).thenReturn(Optional.of(genMember()));

        verify(memberRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> memberService.update(id, inputMemberDto));
    }

    @Test
    public void updateNotExistTest() {
        Integer id = genRandomId();
        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.empty());
        verify(memberRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> memberService.update(id, any()));
    }

    @Test
    public void updateExistNullStatusTest() throws JsonProcessingException {
        Member inputMember = genNullStatusMember(null);
        Integer id = genRandomId();
        Member oldMember = genActiveMember(id);

        Member outputMember = copyMember(inputMember);
        outputMember.setId(id);
        String hashPassword = passwordEncoder.encode(inputMember.getPassword());
        outputMember.setPassword(hashPassword);
        outputMember.setStatus(oldMember.getStatus());

        CreateMemberDto inputMemberDto = mapToCreateDto(inputMember);
        MemberDto outputMemberDto = mapToDto(outputMember);

        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.of(oldMember));
        when(mapper.toEntity(inputMemberDto)).thenReturn(inputMember);
        when(mapper.toDto(inputMember)).thenReturn(outputMemberDto);
        when(passwordEncoder.encode(inputMember.getPassword())).thenReturn(hashPassword);

        MemberDto actualOutputMemberDto = memberService.update(id, inputMemberDto);
        verify(memberRepositoryJpa).save(argThat(m -> m.hashCode() == outputMember.hashCode()));
        Assertions.assertEquals(outputMemberDto.hashCode(), actualOutputMemberDto.hashCode());
    }

    @Test
    public void updateAlreadyRemovedTest() {
        Integer id = genRandomId();
        when(memberRepositoryJpa.findById(id)).thenReturn(Optional.of(genRemovedMember(id)));
        verify(memberRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> memberService.update(id, any()));
    }

    @Test
    public void removeExistTest() throws JsonProcessingException {
        Member inputMember = genActiveMember(genRandomId());
        MemberDto outputMemberDto = mapToDto(inputMember);
        outputMemberDto.setStatus(MemberStatus.REMOVED);

        when(memberRepositoryJpa.findById(inputMember.getId())).thenReturn(Optional.of(inputMember));
        when(memberRepositoryJpa.removeById(inputMember.getId())).thenReturn(1);
        when(mapper.toDto(inputMember)).thenReturn(outputMemberDto);

        MemberDto actualOutputMemberDto = memberService.remove(inputMember.getId());
        Assertions.assertEquals(outputMemberDto.hashCode(), actualOutputMemberDto.hashCode());
    }

    @Test
    public void removeNotExistTest() {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.empty());
        verify(memberRepositoryJpa, times(0)).removeById(any());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> memberService.remove(any()));
    }

    @Test
    public void removeAlreadyRemovedTest() {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(genRemovedMember(genRandomId())));
        verify(memberRepositoryJpa, times(0)).removeById(any());
        Assertions.assertThrows(PropertyValueException.class, () -> memberService.remove(any()));
    }

    @Test
    public void findTest() throws JsonProcessingException {
        Member member = genActiveMember(genRandomId());
        List<Member> members = new ArrayList<>();
        members.add(member);

        List<MemberDto> findResult = new ArrayList<>();
        findResult.add(mapToDto(member));

        when(memberRepositoryJpa.findAllByTextAndStatus(member.getFirstName(), MemberStatus.ACTIVE)).thenReturn(members);
        when(mapper.toListDto(members)).thenReturn(findResult);

        List<MemberDto> actualFindResult = memberService.find(member.getFirstName());
        Assertions.assertEquals(findResult.hashCode(), actualFindResult.hashCode());
    }

    @Test
    public void loadUserByUsernameExistTest() {
        Member member = genActiveMember(genRandomId());
        when(memberRepositoryJpa.findByAccount(member.getAccount())).thenReturn(Optional.of(member));
        MemberDetails memberDetails = (MemberDetails) memberService.loadUserByUsername(member.getAccount());
        Assertions.assertEquals(member.hashCode(), memberDetails.getMember().hashCode());
    }

    @Test
    public void loadUserByUsernameNotExistTest() {
        when(memberRepositoryJpa.findByAccount(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername(any()));
    }

    @Test
    public void initAdminExistTest() {
        Member member = genActiveMember(genRandomId());
        when(memberRepositoryJpa.findByAccount("admin")).thenReturn(Optional.of(member));
        memberService.initAdmin();
        verify(memberRepositoryJpa, times(0)).save(any());
    }

    @Test
    public void initAdminNotExistTest() {
        String hashPassword = passwordEncoder.encode("admin");
        Member admin = Member.builder()
                .lastName("admin")
                .firstName("admin")
                .status(MemberStatus.ACTIVE)
                .account("admin")
                .password(hashPassword).build();

        when(memberRepositoryJpa.findByAccount("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin")).thenReturn(hashPassword);
        memberService.initAdmin();
        verify(memberRepositoryJpa).save(argThat(m -> m.hashCode() == admin.hashCode()));
    }
}
