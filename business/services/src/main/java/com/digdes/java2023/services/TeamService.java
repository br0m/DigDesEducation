package com.digdes.java2023.services;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public interface TeamService {

    TeamMemberDto add(@Valid TeamMemberDto teamMemberDto);

    TeamMemberDto remove(@NotEmpty String codename, @NotEmpty Integer id);

    TeamDto getProjectMembers(@NotEmpty String codename);
}
