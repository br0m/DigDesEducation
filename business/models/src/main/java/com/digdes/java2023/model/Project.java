package com.digdes.java2023.model;

import com.digdes.java2023.dto.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "codename", nullable = false, unique = true, length = 50)
    private String codename;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProjectStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id) && codename.equals(project.codename) && title.equals(project.title) && description.equals(project.description) && status == project.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codename, title, description, status);
    }
}