package com.digdes.java2023.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Component
@Schema(description = "DTO для добавления нового сотрудника")
public class CreateMemberDto extends MemberDto{

    @Schema(description = "Пароль сотрудника")
    @NotBlank
    private String password;
}
