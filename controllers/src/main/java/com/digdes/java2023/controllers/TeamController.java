package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.services.TeamService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@Tag(name = "TeamController", description = "Контроллер участников проектных команд")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "Добавление участника проекта")
    @PostMapping("/add")
    public TeamMemberDto create(@RequestBody TeamMemberDto teamMemberDto) {
        return teamService.add(teamMemberDto);
    }

    @Operation(summary = "Удаление участника проекта")
    @DeleteMapping("/{codename}/{id}")
    public TeamMemberDto remove(@PathVariable String codename, @PathVariable Integer id) {
        return teamService.remove(codename, id);
    }

    @Operation(summary = "Поиск всех участников проекта по кодовому названию")
    @GetMapping("/find")
    public TeamDto find(@RequestParam String codename) {
        return teamService.getProjectMembers(codename);
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
