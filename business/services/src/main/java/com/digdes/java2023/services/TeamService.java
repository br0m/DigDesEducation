package com.digdes.java2023.services;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface TeamService {

    TeamMemberDto add(@Valid TeamMemberDto teamMemberDto);

    TeamMemberDto remove(@NotEmpty String codename, @NotNull Integer id);

    TeamDto getProjectMembers(@NotEmpty String codename);
}
