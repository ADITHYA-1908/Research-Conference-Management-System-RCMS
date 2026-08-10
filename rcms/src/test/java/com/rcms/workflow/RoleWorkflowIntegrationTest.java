package com.rcms.workflow;

import com.rcms.dto.RegisterDto;
import com.rcms.entity.Certificate;
import com.rcms.entity.Conference;
import com.rcms.entity.Paper;
import com.rcms.entity.Review;
import com.rcms.entity.User;
import com.rcms.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RoleWorkflowIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CertificateService certificateService;

    @Test
    @DisplayName("End-to-End Prototype Workflow across Admin, Author, Reviewer, and Public Roles")
    void testCompleteMultiRoleWorkflow() throws Exception {
        // STEP 1: ADMIN ROLE - Create Conference
        Conference conf = new Conference();
        conf.setTitle("International Conference on Cloud & Distributed Systems 2026");
        conf.setTheme("Cloud Native Architectures & Distributed Systems");
        conf.setLocation("Virtual / Main Campus Auditorium");
        conf.setStartDate(LocalDate.of(2026, 11, 10));
        conf.setEndDate(LocalDate.of(2026, 11, 12));
        conf.setSubmissionDeadline(LocalDate.of(2026, 10, 31));
        conf.setStatus("OPEN");
        Conference savedConf = conferenceService.save(conf);
        assertNotNull(savedConf.getId());

        // STEP 2: AUTHOR ROLE - Register Author & Submit Manuscript
        RegisterDto authorDto = new RegisterDto();
        authorDto.setName("Dr. Alan Turing");
        authorDto.setEmail("alan.turing@cambridge.edu");
        authorDto.setPassword("authorPass123");
        User author = userService.registerAuthor(authorDto);
        assertEquals("AUTHOR", author.getRole());

        MockMultipartFile pdfFile = new MockMultipartFile(
                "file", "computable_numbers.pdf", "application/pdf",
                "%PDF-1.5 Sample Manuscript Content".getBytes()
        );

        Paper paper = new Paper();
        paper.setTitle("On Computable Numbers with an Application to the Entscheidungsproblem");
        paper.setAbstractText("This paper introduces the concept of universal computing machines...");
        paper.setKeywords("Computation, Turing Machine, Entscheidungsproblem");

        Paper submittedPaper = paperService.submitPaper(paper, pdfFile, author, savedConf);
        assertEquals("SUBMITTED", submittedPaper.getStatus());
        assertNotNull(submittedPaper.getId());

        // STEP 3: ADMIN & REVIEWER ROLE - Register Reviewer, Assign Paper & Submit Peer Evaluation
        RegisterDto reviewerDto = new RegisterDto();
        reviewerDto.setName("Prof. Grace Hopper");
        reviewerDto.setEmail("grace.hopper@navy.mil");
        reviewerDto.setPassword("reviewerPass123");
        User reviewerUser = userService.registerAuthor(reviewerDto);
        User reviewer = userService.updateRole(reviewerUser.getId(), "REVIEWER");
        assertEquals("REVIEWER", reviewer.getRole());

        Review assignedReview = reviewService.assignReviewer(submittedPaper.getId(), reviewer.getId());
        assertEquals("PENDING", assignedReview.getStatus());

        // Verify Paper status changed to UNDER_REVIEW
        Optional<Paper> underReviewPaper = paperService.findById(submittedPaper.getId());
        assertTrue(underReviewPaper.isPresent());
        assertEquals("UNDER_REVIEW", underReviewPaper.get().getStatus());

        // Reviewer submits score and recommendation
        Review completedReview = reviewService.submitReview(assignedReview.getId(), 98, "Groundbreaking foundational paper.", "ACCEPT");
        assertEquals("COMPLETED", completedReview.getStatus());
        assertEquals(98, completedReview.getScore());

        // STEP 4: ADMIN ROLE - Accept Paper & Issue Certificate
        Paper acceptedPaper = paperService.updateStatus(submittedPaper.getId(), "ACCEPTED");
        assertEquals("ACCEPTED", acceptedPaper.getStatus());

        Certificate issuedCert = certificateService.issueCertificate(author, savedConf, acceptedPaper, "PRESENTATION");
        assertNotNull(issuedCert.getCertificateCode());
        assertEquals("PRESENTATION", issuedCert.getType());

        // STEP 5: PUBLIC ROLE - Verify Certificate via Code Lookup
        Optional<Certificate> verifiedCert = certificateService.findByCode(issuedCert.getCertificateCode());
        assertTrue(verifiedCert.isPresent());
        assertEquals("Dr. Alan Turing", verifiedCert.get().getUser().getName());
        assertEquals("International Conference on Cloud & Distributed Systems 2026", verifiedCert.get().getConference().getTitle());
    }
}
