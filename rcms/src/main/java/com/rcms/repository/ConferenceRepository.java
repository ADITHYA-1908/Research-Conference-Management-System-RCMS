package com.rcms.repository;

import com.rcms.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByStatus(String status);
    List<Conference> findAllByOrderByStartDateDesc();
}
