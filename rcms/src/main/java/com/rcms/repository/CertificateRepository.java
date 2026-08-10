package com.rcms.repository;

import com.rcms.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUserId(Long userId);
    List<Certificate> findByConferenceId(Long conferenceId);
    Optional<Certificate> findByCertificateCode(String certificateCode);
    boolean existsByUserIdAndConferenceIdAndType(Long userId, Long conferenceId, String type);
}
