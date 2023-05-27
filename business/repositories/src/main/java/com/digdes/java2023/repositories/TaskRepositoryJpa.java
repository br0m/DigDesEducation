package com.digdes.java2023.repositories;


import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepositoryJpa extends JpaRepository<Task, Integer> {

    @Transactional
    @Modifying
    @Query("update Task t set t.title = :title, t.description = :description, t.responsibleMember = :responsibleMember, t.hoursCost = :hoursCost, t.deadline = :deadline, t.author = :author, t.lastchangeDate = :lastchangeDate where t.id = :id")
    int update(@Param("title") String title, @Param("description") String description, @Param("responsibleMember") TeamMember responsibleMember, @Param("hoursCost") Integer hoursCost, @Param("deadline") LocalDateTime deadline, @Param("author") TeamMember author, @Param("lastchangeDate") LocalDateTime lastchangeDate, @Param("id") Integer id);

    @Query("select t from Task t where (t.title = :title OR :title IS NULL) and (t.status = :status OR :status IS NULL) and (t.author.id = :authorId OR :authorId IS NULL) and (t.responsibleMember.id = :responsibleMemberId OR :responsibleMemberId IS NULL) and t.deadline >= :deadline and t.creationDate >= :creationDate")
    List<Task> findByDeadlineAfterAndCreationDateAfter(@Param("title") String title, @Param("status") TaskStatus status, @Param("authorId") Integer authorId, @Param("responsibleMemberId") Integer responsibleMemberId, @Param("deadline") LocalDateTime deadline, @Param("creationDate") LocalDateTime creationDate);

    @Query("select t from Task t where (t.title = :title OR :title IS NULL) and (t.status = :status OR :status IS NULL) and (t.author.id = :authorId OR :authorId IS NULL) and (t.responsibleMember.id = :responsibleMemberId OR :responsibleMemberId IS NULL) and t.deadline <= :deadline and t.creationDate <= :creationDate")
    List<Task> findByDeadlineBeforeAndCreationDateBefore(@Param("title") String title, @Param("status") TaskStatus status, @Param("authorId") Integer authorId, @Param("responsibleMemberId") Integer responsibleMemberId, @Param("deadline") LocalDateTime deadline, @Param("creationDate") LocalDateTime creationDate);

    @Query("select t from Task t where (t.title = :title OR :title IS NULL) and (t.status = :status OR :status IS NULL) and (t.author.id = :authorId OR :authorId IS NULL) and (t.responsibleMember.id = :responsibleMemberId OR :responsibleMemberId IS NULL) and t.deadline <= :deadline and t.creationDate >= :creationDate")
    List<Task> findByDeadlineBeforeAndCreationDateAfter(@Param("title") String title, @Param("status") TaskStatus status, @Param("authorId") Integer authorId, @Param("responsibleMemberId") Integer responsibleMemberId, @Param("deadline") LocalDateTime deadline, @Param("creationDate") LocalDateTime creationDate);

    @Query("select t from Task t where (t.title = :title OR :title IS NULL) and (t.status = :status OR :status IS NULL) and (t.author.id = :authorId OR :authorId IS NULL) and (t.responsibleMember.id = :responsibleMemberId OR :responsibleMemberId IS NULL) and t.deadline >= :deadline and t.creationDate <= :creationDate")
    List<Task> findByDeadlineAfterAndCreationDateBefore(@Param("title") String title, @Param("status") TaskStatus status, @Param("authorId") Integer authorId, @Param("responsibleMemberId") Integer responsibleMemberId, @Param("deadline") LocalDateTime deadline, @Param("creationDate") LocalDateTime creationDate);

    @Transactional
    @Modifying
    @Query("update Task t set t.status = :status where t.id = :id")
    int setStatus(@Param("status") TaskStatus status, @Param("id") Integer id);


}
