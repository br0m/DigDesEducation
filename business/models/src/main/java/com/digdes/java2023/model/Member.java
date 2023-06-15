package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 50)
    private String lastName;

    @Column(name = "patronymic", length = 50)
    private String patronymic;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "account", length = 50)
    private String account;

    @Column(name = "email", length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MemberStatus status;

    @Column(name = "password", nullable = false)
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id) && firstName.equals(member.firstName) && lastName.equals(member.lastName) && patronymic.equals(member.patronymic) && jobTitle.equals(member.jobTitle) && account.equals(member.account) && email.equals(member.email) && status == member.status && password.equals(member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, jobTitle, account, email, status, password);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
