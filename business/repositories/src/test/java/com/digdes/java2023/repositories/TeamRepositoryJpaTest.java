package com.digdes.java2023.repositories;

import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.model.config.ModelConfig;
import com.digdes.java2023.repositories.config.RepositoryConfig;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {RepositoryConfig.class, ModelConfig.class, TeamRepositoryJpa.class, MemberRepositoryJpa.class, ProjectRepositoryJpa.class})
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamRepositoryJpaTest extends Init{
    @Autowired
    private TeamRepositoryJpa teamRepository;

    @Autowired
    private MemberRepositoryJpa memberRepository;

    @Autowired
    private ProjectRepositoryJpa projectRepository;

    TeamMember teamMember;

    @BeforeEach
    public void getTeamMember() {
        teamMember = Instancio.create(TeamMember.class);
        teamMember.setProject(projectRepository.save(teamMember.getProject()));
        teamMember.setMember(memberRepository.save(teamMember.getMember()));
        teamMember = teamRepository.save(teamMember);
    }

    @Test
    public void addTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TeamMember cloneTeamMember = objectMapper.readValue(objectMapper.writeValueAsString(teamMember), TeamMember.class);
        Assertions.assertNotEquals(null, teamMember.getId());
        Assertions.assertEquals(cloneTeamMember.hashCode(), teamMember.hashCode());
    }

    @Test
    public void findByIdTest() {
        Optional<TeamMember> findTeamMember = teamRepository.findById(teamMember.getId());
        assert findTeamMember.isPresent();
        Assertions.assertEquals(teamMember.hashCode(), findTeamMember.get().hashCode());
    }

    @Test
    public void findByProjectCodenameTest() {
        List<TeamMember> projectMembers = new ArrayList<>();
        projectMembers.add(teamMember);
        List<TeamMember> actualProjectMembers = teamRepository.findByProjectCodename(teamMember.getProject().getCodename());
        Assertions.assertEquals(projectMembers.hashCode(), actualProjectMembers.hashCode());
    }

    @Test
    public void findByMemberIdAndProjectCodenameTest() {
        Optional<TeamMember> findTeamMember = teamRepository.findByMemberIdAndProjectCodename(teamMember.getMember().getId(), teamMember.getProject().getCodename());
        assert findTeamMember.isPresent();
        Assertions.assertEquals(teamMember.hashCode(), findTeamMember.get().hashCode());
    }

    @Test
    public void removeByIdTest() {
        int count = teamRepository.removeById(teamMember.getId());
        Optional<TeamMember> findTeamMember = teamRepository.findById(teamMember.getId());
        Assertions.assertEquals(1, count);
        Assertions.assertFalse(findTeamMember.isPresent());
    }
}
