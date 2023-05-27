package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.member.CreateMemberDto;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyValueException.class)
    public ErrorMessage handleException(PropertyValueException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ErrorMessage handleException(ObjectNotFoundException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handleException(HttpMessageNotReadableException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleException(ConstraintViolationException exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
