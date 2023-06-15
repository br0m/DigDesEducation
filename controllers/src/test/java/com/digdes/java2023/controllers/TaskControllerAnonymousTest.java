package com.digdes.java2023.controllers;

import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest
public class TaskControllerAnonymousTest extends TaskOperations {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    public TaskControllerAnonymousTest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @BeforeEach
    public void getTask() {
        task = Instancio.create(Task.class);
    }

    @Test
    public void createAnonymousTest() throws Exception {
        mockMvc.perform(post("/task/save")
                        .content(toJson(mapToDto(task)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateAnonymousTest() throws Exception {
        mockMvc.perform(put("/task/{id}", task.getId())
                        .content(toJson(mapToDto(task)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void setStatusAnonymousTest() throws Exception {
        mockMvc.perform(put("/task/{id}/{status}", task.getId(), task.getStatus()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findAnonymousTest() throws Exception {
        mockMvc.perform(post("/task/find")
                        .content(toJson(mapToDto(task)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
