package com.digdes.java2023.controllers;

import com.digdes.java2023.model.TeamMember;
import com.digdes.java2023.model.config.SecurityConfig;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest
public class TeamControllerSecurityTest extends TeamMemberOperations {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void getTeamMember() {
        teamMember = Instancio.create(TeamMember.class);
        project = teamMember.getProject();
        member = teamMember.getMember();
    }

    @Test
    public void addAnonymousTest() throws Exception {
        mockMvc.perform(post("/team/add")
                        .content(toJson(teamMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void removeAnonymousTest() throws Exception {
        mockMvc.perform(delete("/team/{codename}/{id}", project.getCodename(), member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findTest() throws Exception {
        mockMvc.perform(get("/team/find")
                        .param("codename", project.getCodename()))
                .andExpect(status().isUnauthorized());
    }
}
