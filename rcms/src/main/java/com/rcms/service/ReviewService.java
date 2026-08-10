package com.rcms.service;

import com.rcms.entity.Paper;
import com.rcms.entity.Review;
import com.rcms.entity.User;
import com.rcms.repository.PaperRepository;
import com.rcms.repository.ReviewRepository;
import com.rcms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Review assignReviewer(Long paperId, Long reviewerId) {
        if (reviewRepository.existsByPaperIdAndReviewerId(paperId, reviewerId)) {
            throw new IllegalArgumentException("Paper is already assigned to this reviewer.");
        }

        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found"));
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        Review review = new Review();
        review.setPaper(paper);
        review.setReviewer(reviewer);
        review.setStatus("PENDING");

        // Set paper status to UNDER_REVIEW
        paper.setStatus("UNDER_REVIEW");
        paperRepository.save(paper);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review submitReview(Long reviewId, Integer score, String comments, String recommendation) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review assignment not found"));

        review.setScore(score);
        review.setComments(comments);
        review.setRecommendation(recommendation);
        review.setStatus("COMPLETED");
        review.setReviewedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<Review> findByReviewer(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }

    public List<Review> findByPaper(Long paperId) {
        return reviewRepository.findByPaperId(paperId);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
}
