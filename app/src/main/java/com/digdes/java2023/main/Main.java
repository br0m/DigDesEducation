package com.digdes.java2023.main;

import com.digdes.java2023.dto.enums.MemberRole;
import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.repositories.MemberRepository;
import com.digdes.java2023.repositories.TeamRepository;
import com.digdes.java2023.repositories.impl.MemberRepositoryJdbcImpl;
import com.digdes.java2023.repositories.impl.TeamRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/digdes_project", "postgres", "admin")){

            MemberRepository memberRepository = new MemberRepositoryJdbcImpl(connection);
            Member member = Member.builder().firstName("Иван").lastName("Иванов").patronymic("Иванович").account("ivanov").jobTitle("Developer").email("ivanov@mail.ru").status(MemberStatus.ACTIVE).build();
            member=memberRepository.createMember(member);

            Member member2 = Member.builder().id(member.getId()).firstName(member.getFirstName()).lastName(member.getLastName()).patronymic(member.getPatronymic()).jobTitle("Tester").status(member.getStatus()).build();
            member2=memberRepository.updateMember(member2);

            List<Member> list = memberRepository.findMember(member.getFirstName());

            Member member3 = memberRepository.getById(member2.getId());
            Member member4 = memberRepository.deleteById(member2.getId());
            list = memberRepository.getAll();

            TeamRepository teamRepository = new TeamRepositoryJdbcImpl(connection);
            Project project = new Project();
            project.setId(2);
            Map<Member, MemberRole> map = teamRepository.getProjectMembers(project);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
