package com.digdes.java2023.mapping;

import com.digdes.java2023.TeamMemberOperations;
import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.member.MemberDto;
import com.digdes.java2023.dto.project.ProjectDto;
import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.TeamMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamMappingTest extends TeamMemberOperations {

    private final static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Spy
    ModelMapper modelMapper;
    @Spy
    ProjectMapper projectMapper = new ProjectMapper(new ModelMapper());
    @Spy
    MemberMapper memberMapper = new MemberMapper(new ModelMapper());

    @InjectMocks
    TeamMapper teamMapper;

    @Test
    public void DtoToEntityTest() throws JsonProcessingException {
        TeamMember teamMember = Instancio.create(TeamMember.class);
        teamMember.getMember().setPassword(null);
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        TeamMember actualTeamMember = teamMapper.toEntity(teamMemberDto);
        verify(modelMapper).map(teamMemberDto, TeamMember.class);

        Assertions.assertEquals(teamMember.hashCode(), actualTeamMember.hashCode());
    }

    @Test
    public void EntityToDtoTest() throws JsonProcessingException {
        TeamMember teamMember = Instancio.create(TeamMember.class);
        teamMember.getMember().setPassword(null);
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        TeamMemberDto actualTeamMemberDto = teamMapper.toDto(teamMember);
        verify(modelMapper).map(teamMember, TeamMemberDto.class);

        Assertions.assertEquals(teamMemberDto.hashCode(), actualTeamMemberDto.hashCode());
    }

    @Test
    public void ListEntityToTeamDtoTest() throws JsonProcessingException {
        List<TeamMember> projectMembers = Instancio.ofList(TeamMember.class).size(5).create();
        Project project = projectMembers.get(0).getProject();
        TeamDto teamDto = new TeamDto();

        Map<MemberDto, MemberRole> roles = new HashMap<>();
        teamDto.setProject(objectMapper.readValue(objectMapper.writeValueAsString(project), ProjectDto.class));
        for (TeamMember teamMember : projectMembers) {
            roles.put(objectMapper.readValue(objectMapper.writeValueAsString(teamMember.getMember()), MemberDto.class), teamMember.getRole());
        }
        teamDto.setRoles(roles);

        TeamDto actualTeamDto = teamMapper.toTeamDto(projectMembers);
        Assertions.assertEquals(teamDto.hashCode(), actualTeamDto.hashCode());
    }
}
