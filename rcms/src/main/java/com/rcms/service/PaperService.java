package com.rcms.service;

import com.rcms.entity.Conference;
import com.rcms.entity.Paper;
import com.rcms.entity.User;
import com.rcms.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaperService {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB limit

    @Autowired
    private PaperRepository paperRepository;

    @Value("${app.upload-dir:uploads/papers}")
    private String uploadDir;

    public void validatePaperFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Manuscript PDF file is required. Please upload a valid PDF document.");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("Uploaded file exceeds maximum allowed size of 10MB.");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Invalid file extension. Only PDF documents (.pdf) are permitted.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.equalsIgnoreCase("application/pdf")
                && !contentType.equalsIgnoreCase("application/x-pdf")
                && !contentType.equalsIgnoreCase("application/acrobat")) {
            throw new IllegalArgumentException("Invalid MIME type (" + contentType + "). Uploaded file must be a valid PDF document (application/pdf).");
        }
    }

    public Paper submitPaper(Paper paper, MultipartFile file, User author, Conference conference) throws IOException {
        validatePaperFile(file);

        paper.setAuthor(author);
        paper.setConference(conference);
        paper.setStatus("SUBMITTED");

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        paper.setFilePath(fileName);

        return paperRepository.save(paper);
    }

    public List<Paper> findAll() {
        return paperRepository.findAll();
    }

    public List<Paper> findByAuthor(Long authorId) {
        return paperRepository.findByAuthorId(authorId);
    }

    public List<Paper> findByConference(Long conferenceId) {
        return paperRepository.findByConferenceId(conferenceId);
    }

    public Optional<Paper> findById(Long id) {
        return paperRepository.findById(id);
    }

    public Paper updateStatus(Long paperId, String newStatus) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found"));
        paper.setStatus(newStatus);
        return paperRepository.save(paper);
    }

    public long countAll() {
        return paperRepository.count();
    }

    public long countByStatus(String status) {
        return paperRepository.countByStatus(status);
    }
}
