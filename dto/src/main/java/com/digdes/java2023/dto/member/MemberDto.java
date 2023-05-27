package com.digdes.java2023.dto.member;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MemberDto {

    private Integer id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String patronymic;
    private String jobTitle;
    @NotBlank
    private String account;
    private String email;
    private MemberStatus status;

    @Override
    public String toString() {
        String memberJson=null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            memberJson = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return memberJson;
    }
}
