package com.digdes.java2023.dto.team;

import com.digdes.java2023.dto.project.ProjectDto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TeamDto {

    private long id;
    private ProjectDto projectDto;
    private List<TeamMemberDto> teamMemberList;

    public void add(TeamMemberDto teamMemberDto)
    {
        teamMemberList.add(teamMemberDto);
    }
}
