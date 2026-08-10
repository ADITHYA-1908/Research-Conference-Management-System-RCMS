package com.rcms.service;

import com.rcms.entity.Certificate;
import com.rcms.entity.Conference;
import com.rcms.entity.Paper;
import com.rcms.entity.User;
import com.rcms.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    public Certificate issueCertificate(User user, Conference conference, Paper paper, String type) {
        if (certificateRepository.existsByUserIdAndConferenceIdAndType(user.getId(), conference.getId(), type)) {
            throw new IllegalArgumentException(type + " certificate already issued for this user and conference.");
        }
        Certificate cert = new Certificate();
        cert.setUser(user);
        cert.setConference(conference);
        cert.setPaper(paper);
        cert.setType(type);
        return certificateRepository.save(cert);
    }

    public List<Certificate> findByUser(Long userId) {
        return certificateRepository.findByUserId(userId);
    }

    public Optional<Certificate> findById(Long id) {
        return certificateRepository.findById(id);
    }

    public Optional<Certificate> findByCode(String code) {
        return certificateRepository.findByCertificateCode(code.trim().toUpperCase());
    }

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }
}
