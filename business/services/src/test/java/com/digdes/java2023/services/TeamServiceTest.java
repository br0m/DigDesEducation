package com.digdes.java2023.services;

import com.digdes.java2023.TeamMemberOperations;
import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.mapping.TeamMapper;
import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.impl.TeamServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.PropertyValueException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest extends TeamMemberOperations {

    @Spy
    TeamRepositoryJpa teamRepositoryJpa;
    @Spy
    ProjectRepositoryJpa projectRepositoryJpa;
    @Spy
    MemberRepositoryJpa memberRepositoryJpa;
    @Mock
    TeamMapper mapper;
    @InjectMocks
    TeamServiceImpl teamService;

    @BeforeEach
    public void getTeamMember() {
        teamMember = Instancio.create(TeamMember.class);
        project = teamMember.getProject();
        member = teamMember.getMember();
    }

    @Test
    public void addNotInProjectTeamTest() throws JsonProcessingException {
        TeamMemberDto inputTeamMemberDto = mapToDto(teamMember);
        TeamMemberDto outputTeamMemberDto = mapToDto(teamMember);

        when(mapper.toEntity(inputTeamMemberDto)).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.empty());
        when(mapper.toDto(teamMember)).thenReturn(outputTeamMemberDto);

        TeamMemberDto actualOutputTeamMemberDto = teamService.add(inputTeamMemberDto);
        verify(teamRepositoryJpa).save(teamMember);
        Assertions.assertEquals(outputTeamMemberDto.hashCode(), actualOutputTeamMemberDto.hashCode());
    }

    @Test
    public void addMemberNotExistTest() throws JsonProcessingException {
        TeamMemberDto inputTeamMemberDto = mapToDto(teamMember);
        when(mapper.toEntity(inputTeamMemberDto)).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.empty());

        verify(teamRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> teamService.add(inputTeamMemberDto));
    }

    @Test
    public void addProjectNotExistTest() throws JsonProcessingException {
        TeamMemberDto inputTeamMemberDto = mapToDto(teamMember);
        when(mapper.toEntity(inputTeamMemberDto)).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.empty());

        verify(teamRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> teamService.add(inputTeamMemberDto));
    }

    @Test
    public void addAlreadyInProjectTeamTest() throws JsonProcessingException {
        TeamMemberDto inputTeamMemberDto = mapToDto(teamMember);

        when(mapper.toEntity(inputTeamMemberDto)).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.of(teamMember));

        verify(teamRepositoryJpa, times(0)).save(any());
        Assertions.assertThrows(PropertyValueException.class, () -> teamService.add(inputTeamMemberDto));
    }

    @Test
    public void removeExistTest() throws JsonProcessingException {
        TeamMemberDto outputTeamMemberDto = mapToDto(teamMember);

        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.of(teamMember));
        when(teamRepositoryJpa.removeById(teamMember.getId())).thenReturn(1);
        when(mapper.toDto(teamMember)).thenReturn(outputTeamMemberDto);

        TeamMemberDto actualOutputTeamMemberDto = teamService.remove(project.getCodename(), member.getId());
        Assertions.assertEquals(outputTeamMemberDto.hashCode(), actualOutputTeamMemberDto.hashCode());
    }

    @Test
    public void removeNotExistTest() {
        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.empty());
        verify(teamRepositoryJpa, times(0)).removeById(any());
        Assertions.assertThrows(PropertyValueException.class, () -> teamService.remove(project.getCodename(), member.getId()));
    }

    @Test
    public void getProjectMembersTest() throws JsonProcessingException {
        List<TeamMember> projectMembers = new ArrayList<>();
        projectMembers.add(teamMember);
        TeamDto teamDto = mapToTeamDto(projectMembers);

        when(teamRepositoryJpa.findByProjectCodename(project.getCodename())).thenReturn(projectMembers);
        when(mapper.toTeamDto(projectMembers)).thenReturn(teamDto);

        TeamDto actualTeamDto = teamService.getProjectMembers(project.getCodename());
        Assertions.assertEquals(teamDto.hashCode(), actualTeamDto.hashCode());
    }
}
