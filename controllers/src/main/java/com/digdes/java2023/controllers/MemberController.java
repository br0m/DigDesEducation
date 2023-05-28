package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "MemberController", description = "Контроллер сотрудников")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Добавление нового сотрудника")
    @PostMapping("/save")
    public MemberDto create(@RequestBody CreateMemberDto memberDto) {
        return memberService.create(memberDto);
    }

    @Operation(summary = "Обновление данных сотрудника")
    @PutMapping("/{id}")
    public MemberDto update(@PathVariable Integer id, @RequestBody CreateMemberDto memberDto) {
        return memberService.update(id, memberDto);
    }

    @Operation(summary = "Получение сотрудника по ID")
    @GetMapping("/{id}")
    public MemberDto getById(@PathVariable Integer id) {
        return memberService.getById(id);
    }

    @Operation(summary = "Удаление сотрудника")
    @DeleteMapping("/{id}")
    public MemberDto remove(@PathVariable Integer id) {
        return memberService.remove(id);
    }

    @Operation(summary = "Поиск сотрудников по ФИО, аккаунту, email среди активных")
    @GetMapping("/find")
    public List<MemberDto> find(@RequestParam String text) {
        return memberService.find(text);
    }

}
