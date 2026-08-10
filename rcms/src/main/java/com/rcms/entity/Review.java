package com.rcms.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    private Integer score; // 1 to 10 scale

    @Column(columnDefinition = "TEXT")
    private String comments;

    private String recommendation; // "ACCEPT", "MINOR_REVISION", "MAJOR_REVISION", "REJECT"

    @Column(nullable = false, length = 50)
    private String status; // "PENDING", "COMPLETED"

    private LocalDateTime reviewedAt;
}
