package com.digdes.java2023.repositories.impl;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import com.digdes.java2023.repositories.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryJdbcImpl implements MemberRepository {
    private final String url;
    private final String username;
    private final String password;

    public MemberRepositoryJdbcImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Member createMember(Member member) {
        int rowCount = 0;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(" INSERT INTO member (lastname, firstname, patronymic, job_title, account, email, status) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(statement, member);
            rowCount = statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next())
                member.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowCount == 1 ? member : null;
    }

    @Override
    public Member updateMember(Member member) {
        int rowCount = 0;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE member SET lastname=?, firstname=?, patronymic=?, job_title=?, account=?, email=?, status=? WHERE id=?")) {
            prepareStatement(statement, member);
            statement.setLong(8, member.getId());
            rowCount = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowCount == 1 ? member : null;
    }

    @Override
    public List<Member> findMember(String findText) {
        List<Member> members = new ArrayList<>();
        findText = "%" + findText + "%";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM member WHERE (lastname LIKE ? OR firstname LIKE ? OR patronymic LIKE ? OR account LIKE ? OR email LIKE ?) AND status=?")) {
            for (int i = 1; i < 6; i++) {
                statement.setString(i, findText);
            }
            statement.setString(6, MemberStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                members.add(mapping(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public Member getById(long id) {
        Member member = null;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM member WHERE id=?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                member = mapping(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    @Override
    public List<Member> getAll() {
        List<Member> members = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM member");
            while (resultSet.next()) {
                members.add(mapping(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public Member deleteById(long id) {
        int rowCount = 0;
        Member member = getById(id);

        if (member != null && member.getStatus() == MemberStatus.ACTIVE) {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE member SET status=? WHERE id=?")) {
                statement.setString(1, MemberStatus.REMOVED.toString());
                statement.setLong(2, member.getId());
                rowCount = statement.executeUpdate();
                member.setStatus(MemberStatus.REMOVED);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowCount == 1 ? member : null;
    }

    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, username, password);
    }

    private Member mapping(ResultSet resultSet) throws SQLException {

        return Member.builder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("firstname"))
                .lastName(resultSet.getString("lastName"))
                .patronymic(resultSet.getString("patronymic"))
                .account(resultSet.getString("account"))
                .jobTitle(resultSet.getString("job_title"))
                .email(resultSet.getString("email"))
                .status(MemberStatus.valueOf(resultSet.getString("status")))
                .build();
    }

    private void prepareStatement(PreparedStatement statement, Member member) throws SQLException {

        statement.setString(1, member.getLastName());
        statement.setString(2, member.getFirstName());
        statement.setString(3, member.getPatronymic());
        statement.setString(4, member.getJobTitle());
        statement.setString(5, member.getAccount());
        statement.setString(6, member.getEmail());
        statement.setString(7, member.getStatus().toString());
    }
}
