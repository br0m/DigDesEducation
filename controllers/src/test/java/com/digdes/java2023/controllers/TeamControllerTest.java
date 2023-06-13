package com.digdes.java2023.controllers;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.mapping.TeamMapper;
import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.impl.TeamServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TeamController.class, TeamServiceImpl.class, TeamMember.class, GlobalExceptionHandler.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class TeamControllerTest extends TeamMemberOperations{

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamMapper mapper;
    @MockBean
    private TeamRepositoryJpa teamRepositoryJpa;
    @MockBean
    private MemberRepositoryJpa memberRepositoryJpa;
    @MockBean
    private ProjectRepositoryJpa projectRepositoryJpa;

    @BeforeEach
    public void getTeamMember() {
        teamMember = Instancio.create(TeamMember.class);
        project = teamMember.getProject();
        member = teamMember.getMember();
    }

    @Test
    public void addTest() throws Exception {
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        when(mapper.toEntity(any())).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.of(project));
        when(mapper.toDto(teamMember)).thenReturn(teamMemberDto);

        mockMvc.perform(post("/team/add")
                        .content(toJson(teamMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(teamMemberDto)));
    }

    @Test
    public void addMemberNotExistTest() throws Exception {
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        when(mapper.toEntity(any())).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/team/add")
                        .content(toJson(teamMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProjectNotExistTest() throws Exception {
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        when(mapper.toEntity(any())).thenReturn(teamMember);
        when(memberRepositoryJpa.findById(member.getId())).thenReturn(Optional.of(member));
        when(projectRepositoryJpa.findById(project.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/team/add")
                        .content(toJson(teamMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void addInvalidDtoTest(TeamMemberDto teamMemberDto) throws Exception {
        mockMvc.perform(post("/team/add")
                        .content(toJson(teamMemberDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeTest() throws Exception {
        TeamMemberDto teamMemberDto = mapToDto(teamMember);

        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.of(teamMember));
        when(teamRepositoryJpa.removeById(teamMember.getId())).thenReturn(1);
        when(mapper.toDto(teamMember)).thenReturn(teamMemberDto);

        mockMvc.perform(delete("/team/{codename}/{id}", project.getCodename(), member.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(teamMemberDto)));
    }

    @Test
    public void removeNotExistTest() throws Exception {

        when(teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/team/{codename}/{id}", project.getCodename(), member.getId()))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidRemoveParams")
    public void removeInvalidParamsTest(String codename, Integer id) throws Exception {
        mockMvc.perform(delete("/team/{codename}/{id}", codename, id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findTest() throws Exception {
        List<TeamMember> projectMembers = new ArrayList<>();
        projectMembers.add(teamMember);
        TeamDto teamDto = mapToTeamDto(projectMembers);

        when(teamRepositoryJpa.findByProjectCodename(project.getCodename())).thenReturn(projectMembers);
        when(mapper.toTeamDto(projectMembers)).thenReturn(teamDto);

        mockMvc.perform(get("/team/find")
                        .param("codename", project.getCodename()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(toJson(teamDto)));
    }

    private static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.of(nullMemberDto),
                Arguments.of(nullProjectDto),
                Arguments.of(nullRoleDto));
    }

    private static Stream<Arguments> invalidRemoveParams() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of("1", null));
    }
}
