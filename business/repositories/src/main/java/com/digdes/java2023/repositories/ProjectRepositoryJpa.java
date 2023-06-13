package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.ProjectStatus;
import com.digdes.java2023.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepositoryJpa extends JpaRepository<Project, Integer> {

    @Query(value = "SELECT p FROM Project p WHERE (p.codename=:text OR p.title=:text) AND p.status IN :statuses")
    List<Project> findAllByTextAndStatuses(@Param("text") String text, @Param("statuses") List<ProjectStatus> statuses);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Project p SET p.status=:status WHERE p.id=:id")
    int setStatus(@Param("id") Integer id, @Param("status") ProjectStatus status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Project p SET p.codename=:codename, p.title=:title, p.description=:description WHERE p.id=:id")
    int update(@Param("codename") String codename, @Param("title") String title, @Param("description") String description, @Param("id") Integer id);

    Optional<Project> findById(Integer id);

    Optional<Project> findByCodename(String codename);
}
