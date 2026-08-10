package com.rcms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "papers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Paper title is required")
    @Size(min = 5, max = 255, message = "Paper title must be between 5 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Abstract is required")
    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @NotBlank(message = "Keywords are required")
    @Size(max = 255, message = "Keywords cannot exceed 255 characters")
    @Column(length = 255)
    private String keywords;

    private String filePath;

    @Column(nullable = false, length = 50)
    private String status; // "SUBMITTED", "UNDER_REVIEW", "ACCEPTED", "REJECTED", "WITHDRAWN"

    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @PrePersist
    public void onCreate() {
        this.submittedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "SUBMITTED";
        }
    }
}
