package com.digdes.java2023.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CreateMemberDto extends MemberDto{

    @NotBlank
    private String password;
}
