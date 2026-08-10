package com.rcms.service;

import com.rcms.entity.Conference;
import com.rcms.entity.Paper;
import com.rcms.entity.User;
import com.rcms.repository.PaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaperServiceTest {

    @Mock
    private PaperRepository paperRepository;

    @InjectMocks
    private PaperService paperService;

    private User author;
    private Conference conference;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        author.setName("Author Jane");

        conference = new Conference();
        conference.setId(10L);
        conference.setTitle("AI & Robotics 2026");
    }

    @Test
    @DisplayName("Validate PDF - Non-PDF File Throws Exception")
    void testValidatePaperFileNonPdf() {
        MockMultipartFile txtFile = new MockMultipartFile("file", "paper.txt", "text/plain", "content".getBytes());

        assertThrows(IllegalArgumentException.class, () -> paperService.validatePaperFile(txtFile));
    }

    @Test
    @DisplayName("Validate PDF - Valid PDF Passes")
    void testValidatePaperFileValidPdf() {
        MockMultipartFile pdfFile = new MockMultipartFile("file", "manuscript.pdf", "application/pdf", "%PDF-1.4 sample content".getBytes());

        assertDoesNotThrow(() -> paperService.validatePaperFile(pdfFile));
    }

    @Test
    @DisplayName("Update Paper Status - Success")
    void testUpdatePaperStatus() {
        Paper paper = new Paper();
        paper.setId(50L);
        paper.setStatus("SUBMITTED");

        when(paperRepository.findById(50L)).thenReturn(Optional.of(paper));
        when(paperRepository.save(any(Paper.class))).thenAnswer(i -> i.getArgument(0));

        Paper updated = paperService.updateStatus(50L, "ACCEPTED");

        assertEquals("ACCEPTED", updated.getStatus());
    }
}
