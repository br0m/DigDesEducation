package com.digdes.java2023.mapping;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.model.TeamMember;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamMapper {

    private final ModelMapper modelMapper;
    private final ProjectMapper projectMapper;
    private final MemberMapper memberMapper;

    public TeamMember toEntity(TeamMemberDto teamMemberDto) {
        return modelMapper.map(teamMemberDto, TeamMember.class);
    }

    public TeamMemberDto toDto(TeamMember teamMember) {
        return modelMapper.map(teamMember, TeamMemberDto.class);
    }

    public TeamDto toTeamDto(List<TeamMember> teamMembers) {

        if (teamMembers.isEmpty())
            return null;
        TeamDto teamDto = new TeamDto();
        Map<MemberDto, MemberRole> roles = new HashMap<>();
        teamDto.setProject(projectMapper.toDto(teamMembers.get(0).getProject()));
        for (TeamMember teamMember : teamMembers) {
            roles.put(memberMapper.toDto(teamMember.getMember()), teamMember.getRole());
        }
        teamDto.setRoles(roles);

        return teamDto;
    }
}