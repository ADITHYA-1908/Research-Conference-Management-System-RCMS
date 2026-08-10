package com.rcms.repository;

import com.rcms.entity.ConferenceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRegistrationRepository extends JpaRepository<ConferenceRegistration, Long> {
    List<ConferenceRegistration> findByUserId(Long userId);
    List<ConferenceRegistration> findByConferenceId(Long conferenceId);
    boolean existsByUserIdAndConferenceId(Long userId, Long conferenceId);
    Optional<ConferenceRegistration> findByUserIdAndConferenceId(Long userId, Long conferenceId);
}
