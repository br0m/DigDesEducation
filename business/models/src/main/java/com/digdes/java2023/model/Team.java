package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class Team {

    private long id;
    private Project project;
    private Map<Member, MemberRole> roles;
}
