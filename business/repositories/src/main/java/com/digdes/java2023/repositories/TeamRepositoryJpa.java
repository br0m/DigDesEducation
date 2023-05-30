package com.digdes.java2023.repositories;

import com.digdes.java2023.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepositoryJpa extends JpaRepository<TeamMember, Integer> {

    List<TeamMember> findByProjectCodename(String codename);

    Optional<TeamMember> findByMemberIdAndProjectCodename(Integer id, String codename);

    @Transactional
    @Modifying
    int removeById(Integer id);

    Optional<TeamMember> findById(Integer id);

    @Query("select t from TeamMember t where t.member.id = :memberID and t.project.codename = :projectCodename")
    Optional<TeamMember> findTeamMemberByMemberIdAndProjectCodename(@Param("memberID") Integer memberID, @Param("projectCodename") String projectCodename);
}