package com.rcms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "conferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Conference title is required")
    @Size(min = 5, max = 255, message = "Conference title must be between 5 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @Size(max = 255, message = "Theme cannot exceed 255 characters")
    @Column(length = 255)
    private String theme;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 255, message = "Location cannot exceed 255 characters")
    @Column(length = 255)
    private String location;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Submission deadline is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate submissionDeadline;

    @Column(nullable = false, length = 50)
    private String status; // "UPCOMING", "OPEN", "CLOSED"
}
