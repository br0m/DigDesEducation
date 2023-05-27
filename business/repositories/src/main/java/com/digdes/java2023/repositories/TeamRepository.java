package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;

import java.util.Map;

public interface TeamRepository {

    Map<Member, MemberRole> getProjectMembers(Project project);
}
