package com.digdes.java2023.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO для добавления нового сотрудника")
public class CreateMemberDto extends MemberDto{

    @Schema(description = "Пароль сотрудника")
    @NotBlank
    private String password;
}
