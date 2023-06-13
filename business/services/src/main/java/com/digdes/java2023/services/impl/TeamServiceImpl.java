package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.team.TeamDto;
import com.digdes.java2023.dto.team.TeamMemberDto;
import com.digdes.java2023.mapping.TeamMapper;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.repositories.MemberRepositoryJpa;
import com.digdes.java2023.repositories.ProjectRepositoryJpa;
import com.digdes.java2023.repositories.TeamRepositoryJpa;
import com.digdes.java2023.services.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TeamServiceImpl implements TeamService {

    private final TeamRepositoryJpa teamRepositoryJpa;
    private final MemberRepositoryJpa memberRepositoryJpa;
    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final TeamMapper mapper;

    @Override
    public TeamMemberDto add(@Valid TeamMemberDto teamMemberDto) {
        TeamMember teamMember = mapper.toEntity(teamMemberDto);
        checkMemberAndProjectExist(teamMember);
        if (getTeamMember(teamMember.getMember(), teamMember.getProject()) != null)
            throw new PropertyValueException("Already a member of the project team", "team", "member");
        teamRepositoryJpa.save(teamMember);
        return mapper.toDto(teamMember);
    }

    @Override
    public TeamMemberDto remove(@NotEmpty String codename, @NotNull Integer id) {
        TeamMember teamMember = teamRepositoryJpa.findByMemberIdAndProjectCodename(id, codename).orElseThrow(() -> new PropertyValueException("Not a member of the project team", "team", "member"));
        int count = teamRepositoryJpa.removeById(teamMember.getId());
        return count == 1 ? mapper.toDto(teamMember) : null;
    }

    @Override
    public TeamDto getProjectMembers(@NotEmpty String codename) {
        List<TeamMember> projectMembers = teamRepositoryJpa.findByProjectCodename(codename);
        return mapper.toTeamDto(projectMembers);
    }

    private void checkMemberAndProjectExist(TeamMember teamMember) {
        teamMember.setMember(memberRepositoryJpa.findById(teamMember.getMember().getId()).orElseThrow(() -> new PropertyValueException("No such member exist", "member", "id")));
        teamMember.setProject(projectRepositoryJpa.findById(teamMember.getProject().getId()).orElseThrow(() -> new PropertyValueException("No such project exist", "project", "id")));
    }

    private TeamMember getTeamMember(Member member, Project project) {
        return teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename()).orElse(null);
    }
}
