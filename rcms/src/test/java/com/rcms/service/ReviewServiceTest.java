package com.rcms.service;

import com.rcms.entity.Paper;
import com.rcms.entity.Review;
import com.rcms.entity.User;
import com.rcms.repository.PaperRepository;
import com.rcms.repository.ReviewRepository;
import com.rcms.repository.UserRepository;
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
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PaperRepository paperRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Paper paper;
    private User reviewer;

    @BeforeEach
    void setUp() {
        paper = new Paper();
        paper.setId(100L);
        paper.setTitle("Deep Learning in Academic Peer Review");
        paper.setStatus("SUBMITTED");

        reviewer = new User();
        reviewer.setId(200L);
        reviewer.setName("Prof. Charles Babbage");
        reviewer.setRole("REVIEWER");
    }

    @Test
    @DisplayName("Assign Reviewer - Success Sets Paper Status to UNDER_REVIEW")
    void testAssignReviewerSuccess() {
        when(reviewRepository.existsByPaperIdAndReviewerId(100L, 200L)).thenReturn(false);
        when(paperRepository.findById(100L)).thenReturn(Optional.of(paper));
        when(userRepository.findById(200L)).thenReturn(Optional.of(reviewer));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        Review assigned = reviewService.assignReviewer(100L, 200L);

        assertNotNull(assigned);
        assertEquals("PENDING", assigned.getStatus());
        assertEquals("UNDER_REVIEW", paper.getStatus());
        verify(paperRepository, times(1)).save(paper);
    }

    @Test
    @DisplayName("Submit Review - Success Sets Status COMPLETED")
    void testSubmitReviewSuccess() {
        Review review = new Review();
        review.setId(300L);
        review.setStatus("PENDING");

        when(reviewRepository.findById(300L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        Review completed = reviewService.submitReview(300L, 95, "Excellent research paper.", "ACCEPT");

        assertEquals("COMPLETED", completed.getStatus());
        assertEquals(95, completed.getScore());
        assertEquals("ACCEPT", completed.getRecommendation());
        assertNotNull(completed.getReviewedAt());
    }
}
