package com.digdes.java2023.dto.member;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@Component
@Schema(description = "DTO сотрудника")
public class MemberDto {

    @Schema(description = "Уникальный ID сотрудника")
    private Integer id;

    @Schema(description = "Имя")
    @NotBlank
    private String firstName;

    @Schema(description = "Фамилия")
    @NotBlank
    private String lastName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "Должность")
    private String jobTitle;

    @Schema(description = "Аккаунт (уникальный среди активных)")
    @NotBlank
    private String account;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Статус: ACTIVE, REMOVED")
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
