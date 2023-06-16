package com.digdes.java2023.controllers;

import com.digdes.java2023.model.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class MemberControllerAnonymousTest extends MemberOperations{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    public MemberControllerAnonymousTest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @BeforeEach
    public void getMember() {
        member = genMember();
    }

    @Test
    public void createAnonymousTest() throws Exception {
        mockMvc.perform(post("/member/save")
                        .content(toJson(member))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateAnonymousTest() throws Exception {
        mockMvc.perform(put("/member/{id}", member.getId())
                        .content(toJson(member))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getByIdAnonymousTest() throws Exception {
        mockMvc.perform(get("/member/{id}", member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void removeAnonymousTest() throws Exception {
        mockMvc.perform(delete("/member/{id}", member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findAnonymousTest() throws Exception {
        mockMvc.perform(get("/member/find")
                        .param("text", member.getFirstName()))
                .andExpect(status().isUnauthorized());
    }
}
