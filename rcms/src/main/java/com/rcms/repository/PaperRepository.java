package com.rcms.repository;

import com.rcms.entity.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByAuthorId(Long authorId);
    List<Paper> findByConferenceId(Long conferenceId);
    List<Paper> findByStatus(String status);
    long countByStatus(String status);
}
