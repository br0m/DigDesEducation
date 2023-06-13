package com.digdes.java2023.controllers;

import com.digdes.java2023.model.config.SecurityConfig;
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
public class  ProjectControllerSecurityTest extends ProjectOperations{

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void getProject() {
        project = genProject();
    }

    @Test
    public void createAnonymousTest() throws Exception {
        mockMvc.perform(post("/project/save")
                        .content(toJson(project))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateAnonymousTest() throws Exception {
        mockMvc.perform(put("/project/{id}", genRandomId())
                        .content(toJson(project))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void setStatusAnonymousTest() throws Exception {
        mockMvc.perform(patch("/project/{id}/{status}", project.getId(), project.getStatus()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findTest() throws Exception {
        mockMvc.perform(get("/project/find")
                        .param("text", project.getTitle())
                        .param("statuses", project.getStatus().toString()))
                .andExpect(status().isUnauthorized());
    }
}
