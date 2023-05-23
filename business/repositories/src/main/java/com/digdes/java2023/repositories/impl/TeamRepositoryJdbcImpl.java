package com.digdes.java2023.repositories.impl;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.repositories.TeamRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TeamRepositoryJdbcImpl implements TeamRepository {

    Connection connection;

    public TeamRepositoryJdbcImpl(Connection connection) {
    this.connection=connection;
    }

    @Override
    public Map<Member, MemberRole> getProjectMembers(Project project) {
        Map<Member, MemberRole> projectMembers = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT member.*, team.role FROM team LEFT JOIN member ON team.member_id=member.id WHERE team.project_id=?")) {
            statement.setLong(1, project.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Member member=Member.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("firstname"))
                        .lastName(resultSet.getString("lastName"))
                        .patronymic(resultSet.getString("patronymic"))
                        .account(resultSet.getString("account"))
                        .jobTitle(resultSet.getString("job_title"))
                        .email(resultSet.getString("email"))
                        .status(MemberStatus.valueOf(resultSet.getString("status")))
                        .build();
                MemberRole memberRole= MemberRole.valueOf(resultSet.getString("role"));
                projectMembers.put(member, memberRole);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectMembers;
    }
}
