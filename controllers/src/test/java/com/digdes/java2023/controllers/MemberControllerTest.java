package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.mapping.MemberMapper;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.services.impl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MemberController.class, MemberServiceImpl.class, Member.class, GlobalExceptionHandler.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest extends MemberOperations {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberMapper mapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MemberRepositoryJpa memberRepositoryJpa;

    @Test
    public void createTest() throws Exception {

        Member inputMember = genMember();
        MemberDto outputMember = mapToDto(inputMember);
        outputMember.setId(genRandomId());
        outputMember.setStatus(MemberStatus.ACTIVE);

        when(mapper.toEntity(any())).thenReturn(inputMember);
        when(mapper.toDto(inputMember)).thenReturn(outputMember);

        mockMvc.perform(post("/member/save")
                        .content(toJson(inputMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputMember)));
    }

    @Test
    public void createExistTest() throws Exception {

        Member inputMember = genMember();

        when(mapper.toEntity(any())).thenReturn(inputMember);
        when(memberRepositoryJpa.findByAccountAndStatus(any(), any())).thenReturn(Optional.of(inputMember));

        mockMvc.perform(post("/member/save")
                        .content(toJson(inputMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void createInvalidDtoTest(CreateMemberDto createMemberDto) throws Exception {
        mockMvc.perform(post("/member/save")
                        .content(toJson(createMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateExistTest() throws Exception {
        Member inputMember = genMember();
        Integer id = inputMember.getId();
        MemberDto outputMember = mapToDto(inputMember);

        when(mapper.toEntity(any())).thenReturn(inputMember);
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(genActiveMember(id)));
        when(memberRepositoryJpa.findByAccountAndStatus(any(), any())).thenReturn(Optional.empty());
        when(mapper.toDto(any())).thenReturn(outputMember);

        mockMvc.perform(put("/member/{id}", id)
                    .content(toJson(inputMember))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().json(toJson(outputMember)));
    }

    @Test
    public void updateNotExistTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/member/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAlreadyRemovedTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(genRemovedMember(genRandomId())));

        mockMvc.perform(put("/member/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void updateInvalidDtoTest(CreateMemberDto createMemberDto) throws Exception {
        mockMvc.perform(put("/member/{id}", createMemberDto.getId())
                        .content(toJson(createMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateNullIdTest() throws Exception {
        mockMvc.perform(put("/member/")
                        .content(toJson(genActiveMember(genRandomId())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getByIdExistTest() throws Exception {
        Member inputMember = genMember();
        MemberDto outputMember = mapToDto(inputMember);

        when(mapper.toEntity(any())).thenReturn(inputMember);
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(inputMember));
        when(mapper.toDto(any())).thenReturn(outputMember);

        mockMvc.perform(get("/member/{id}", inputMember.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputMember)));
    }

    @Test
    public void getByIdNotExistTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/member/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdNullIdTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/member/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void removeExistTest() throws Exception {
        Member inputMember = genActiveMember(genRandomId());
        MemberDto outputMember = mapToDto(inputMember);
        outputMember.setStatus(MemberStatus.REMOVED);

        when(mapper.toEntity(any())).thenReturn(inputMember);
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(inputMember));
        when(memberRepositoryJpa.removeById(any())).thenReturn(1);
        when(mapper.toDto(any())).thenReturn(outputMember);

        mockMvc.perform(delete("/member/{id}", inputMember.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(outputMember)));
    }

    @Test
    public void removeNotExistTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/member/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeAlreadyRemovedTest() throws Exception {
        when(memberRepositoryJpa.findById(any())).thenReturn(Optional.of(genRemovedMember(genRandomId())));

        mockMvc.perform(delete("/member/{id}", genRandomId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeNullIdTest() throws Exception {
        mockMvc.perform(delete("/member/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findTest() throws Exception {
        List<MemberDto> membersDto = new ArrayList<>();
        Member member = genActiveMember(genRandomId());
        membersDto.add(mapToDto(member));

        when(mapper.toListDto(any())).thenReturn(membersDto);

        mockMvc.perform(get("/member/find")
                        .param("text", member.getFirstName()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(membersDto)));
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.of(blankFirstNameMember),
                Arguments.of(blankLastNameMember),
                Arguments.of(blankAccountMember),
                Arguments.of(blankPasswordMember));
    }
}
