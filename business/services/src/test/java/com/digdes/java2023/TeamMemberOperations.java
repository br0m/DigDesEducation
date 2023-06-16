package com.digdes.java2023;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.TeamMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMemberOperations {
    private final static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected TeamMember teamMember;
    protected Member member;
    protected Project project;

    protected TeamMemberDto mapToDto(TeamMember teamMember) throws JsonProcessingException {
        return objectMapper.readValue(objectMapper.writeValueAsString(teamMember), TeamMemberDto.class);
    }

    protected TeamDto mapToTeamDto(List<TeamMember> projectMembers) throws JsonProcessingException {
        TeamDto teamDto = new TeamDto();
        Map<MemberDto, MemberRole> roles = new HashMap<>();
        teamDto.setProject(objectMapper.readValue(objectMapper.writeValueAsString(project), ProjectDto.class));
        for (TeamMember teamMember : projectMembers) {
            roles.put(objectMapper.readValue(objectMapper.writeValueAsString(member), MemberDto.class), teamMember.getRole());
        }
        teamDto.setRoles(roles);

        return teamDto;
    }
}
