package com.digdes.java2023.repositories;

import com.digdes.java2023.dto.enums.MemberStatus;
import com.digdes.java2023.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepositoryJpa extends JpaRepository<Member, Integer> {
    @Query(value = "SELECT m FROM Member m WHERE (m.lastName LIKE :text OR m.firstName LIKE :text OR m.patronymic LIKE :text OR m.account LIKE :text OR m.email LIKE :text) AND m.status=:status")
    List<Member> findAllByTextAndStatus(@Param("text") String text, @Param("status") MemberStatus status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Member m SET m.status='REMOVED' WHERE m.id=:id")
    int removeById(@Param("id") Integer id);

    Optional<Member> findById(Integer id);

    Optional<Member> findByAccount(String account);
}