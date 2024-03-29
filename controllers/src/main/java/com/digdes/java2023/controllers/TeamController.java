package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/team")
@Tag(name = "TeamController", description = "Контроллер участников проектных команд")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "Добавление участника проекта")
    @PostMapping("/add")
    public TeamMemberDto add(@RequestBody TeamMemberDto teamMemberDto) {
        log.info("New create team member request received");
        return teamService.add(teamMemberDto);
    }

    @Operation(summary = "Удаление участника проекта")
    @DeleteMapping("/{codename}/{id}")
    public TeamMemberDto remove(@PathVariable String codename, @PathVariable Integer id) {
        log.info("New remove team member request received");
        return teamService.remove(codename, id);
    }

    @Operation(summary = "Поиск всех участников проекта по кодовому названию")
    @GetMapping("/find")
    public TeamDto find(@RequestParam String codename) {
        log.info("New search team member request received");
        return teamService.getProjectMembers(codename);
    }

}
