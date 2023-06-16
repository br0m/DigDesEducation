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
import lombok.extern.log4j.Log4j2;
import org.hibernate.PropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Validated
public class  TeamServiceImpl implements TeamService {

    private final TeamRepositoryJpa teamRepositoryJpa;
    private final MemberRepositoryJpa memberRepositoryJpa;
    private final ProjectRepositoryJpa projectRepositoryJpa;
    private final TeamMapper mapper;

    @Override
    public TeamMemberDto add(@Valid TeamMemberDto teamMemberDto) {
        log.info("Creating new team member");
        log.debug(teamMemberDto.toString());

        TeamMember teamMember = mapper.toEntity(teamMemberDto);
        checkMemberAndProjectExist(teamMember);
        if (getTeamMember(teamMember.getMember(), teamMember.getProject()) != null) {
            log.warn("Already a member of the project team");
            throw new PropertyValueException("Already a member of the project team", "team", "member");
        }
        teamRepositoryJpa.save(teamMember);

        log.info("New team member successfully created");
        log.debug(teamMember.toString());
        return mapper.toDto(teamMember);
    }

    @Override
    public TeamMemberDto remove(@NotEmpty String codename, @NotNull Integer id) {
        log.info("Removing team member with member id = " + id + " and project codename = " + codename);

        TeamMember teamMember = teamRepositoryJpa.findByMemberIdAndProjectCodename(id, codename).orElseThrow(() -> new PropertyValueException("Not a member of the project team", "team", "member"));
        int count = teamRepositoryJpa.removeById(teamMember.getId());

        if(count==1) {
            log.info("Team member with member id = " + id + " and project codename = " + codename + " successfully removed");
            log.debug(teamMember.toString());
            return mapper.toDto(teamMember);
        }
        log.warn("Remove error");
        return null;
    }

    @Override
    public TeamDto getProjectMembers(@NotEmpty String codename) {
        log.info("Getting members of project with codename = " + codename);
        List<TeamMember> projectMembers = teamRepositoryJpa.findByProjectCodename(codename);

        log.info("Successfully received members of project with codename = " + codename);
        log.debug(projectMembers.toString());
        return mapper.toTeamDto(projectMembers);
    }

    private void checkMemberAndProjectExist(TeamMember teamMember) {
        Member member = memberRepositoryJpa.findById(teamMember.getMember().getId()).orElse(null);
        if(member==null) {
            log.warn("No such member exist");
            log.debug(teamMember.getMember().toString());
            throw new PropertyValueException("No such member exist", "member", "id");
        }
        teamMember.setMember(member);

        Project project = projectRepositoryJpa.findById(teamMember.getProject().getId()).orElse(null);
        if(project==null) {
            log.warn("No such project exist");
            log.debug(teamMember.getProject().toString());
            throw new PropertyValueException("No such project exist", "project", "id");
        }
        teamMember.setProject(project);

    }

    private TeamMember getTeamMember(Member member, Project project) {
        return teamRepositoryJpa.findByMemberIdAndProjectCodename(member.getId(), project.getCodename()).orElse(null);
    }
}
