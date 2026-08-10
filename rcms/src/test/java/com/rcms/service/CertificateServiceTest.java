package com.rcms.service;

import com.rcms.entity.Certificate;
import com.rcms.entity.Conference;
import com.rcms.entity.Paper;
import com.rcms.entity.User;
import com.rcms.repository.CertificateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateService certificateService;

    private User user;
    private Conference conference;
    private Paper paper;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Author Smith");

        conference = new Conference();
        conference.setId(10L);
        conference.setTitle("CS Conference 2026");

        paper = new Paper();
        paper.setId(100L);
        paper.setTitle("Quantum Computing Paper");
    }

    @Test
    @DisplayName("Issue Certificate - Success")
    void testIssueCertificateSuccess() {
        when(certificateRepository.existsByUserIdAndConferenceIdAndType(1L, 10L, "PRESENTATION")).thenReturn(false);
        when(certificateRepository.save(any(Certificate.class))).thenAnswer(i -> {
            Certificate c = i.getArgument(0);
            c.setCertificateCode("CERT-A1B2C3D4");
            return c;
        });

        Certificate cert = certificateService.issueCertificate(user, conference, paper, "PRESENTATION");

        assertNotNull(cert);
        assertEquals("PRESENTATION", cert.getType());
        assertEquals("CERT-A1B2C3D4", cert.getCertificateCode());
    }

    @Test
    @DisplayName("Find Certificate By Code - Trim and Case Insensitive")
    void testFindByCodeSanitized() {
        Certificate cert = new Certificate();
        cert.setCertificateCode("CERT-12345678");

        when(certificateRepository.findByCertificateCode("CERT-12345678")).thenReturn(Optional.of(cert));

        Optional<Certificate> found = certificateService.findByCode("  cert-12345678  ");

        assertTrue(found.isPresent());
        assertEquals("CERT-12345678", found.get().getCertificateCode());
    }
}
