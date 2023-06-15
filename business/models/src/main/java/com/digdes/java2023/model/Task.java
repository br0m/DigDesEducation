package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsible_member")
    private TeamMember responsibleMember;

    @Column(name = "hours_cost", nullable = false)
    private Integer hoursCost;

    @Column(name = "deadline", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private TeamMember author;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(name = "lastchange_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastchangeDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && title.equals(task.title) && description.equals(task.description) && responsibleMember.equals(task.responsibleMember) && hoursCost.equals(task.hoursCost) && deadline.equals(task.deadline) && status == task.status && author.equals(task.author) && creationDate.equals(task.creationDate) && lastchangeDate.equals(task.lastchangeDate) && project.equals(task.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, responsibleMember, hoursCost, deadline, status, author, creationDate, lastchangeDate, project);
    }
}