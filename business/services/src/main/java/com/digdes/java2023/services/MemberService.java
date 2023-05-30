package com.digdes.java2023.services;

import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface MemberService {

    MemberDto getById(@NotNull Integer id);

    MemberDto create(@Valid CreateMemberDto memberDto);

    MemberDto update(@NotNull Integer id, @Valid CreateMemberDto memberDto);

    MemberDto remove(@NotNull Integer id);

    List<MemberDto> find(@Min(3) String text);

}
