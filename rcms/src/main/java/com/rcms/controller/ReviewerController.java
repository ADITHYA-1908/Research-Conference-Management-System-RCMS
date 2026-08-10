package com.rcms.controller;

import com.rcms.entity.Review;
import com.rcms.entity.User;
import com.rcms.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviewer")
public class ReviewerController {

    @Autowired
    private ReviewService reviewService;

    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User reviewer = getLoggedUser(session);
        List<Review> reviews = reviewService.findByReviewer(reviewer.getId());

        model.addAttribute("reviews", reviews);
        model.addAttribute("totalAssigned", reviews.size());
        model.addAttribute("pendingReviews", reviews.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count());
        model.addAttribute("completedReviews", reviews.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())).count());
        return "reviewer/dashboard";
    }

    @GetMapping("/papers")
    public String listAssignedPapers(HttpSession session, Model model) {
        User reviewer = getLoggedUser(session);
        List<Review> reviews = reviewService.findByReviewer(reviewer.getId());
        model.addAttribute("reviews", reviews);
        return "reviewer/papers";
    }

    @GetMapping("/review-form/{id}")
    public String showReviewForm(@PathVariable("id") Long reviewId, HttpSession session, Model model) {
        User reviewer = getLoggedUser(session);
        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review assignment not found"));

        if (!review.getReviewer().getId().equals(reviewer.getId())) {
            return "redirect:/reviewer/papers?error=unauthorized";
        }

        model.addAttribute("review", review);
        return "reviewer/review-form";
    }

    @PostMapping("/submit-review")
    public String processSubmitReview(@Valid @ModelAttribute("review") Review reviewForm,
                                     BindingResult bindingResult,
                                     @RequestParam("reviewId") Long reviewId,
                                     HttpSession session,
                                     Model model) {
        User reviewer = getLoggedUser(session);
        Review existingReview = reviewService.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review assignment not found"));

        if (!existingReview.getReviewer().getId().equals(reviewer.getId())) {
            return "redirect:/reviewer/papers?error=unauthorized";
        }

        boolean hasErrors = false;
        if (reviewForm.getScore() == null || reviewForm.getScore() < 1 || reviewForm.getScore() > 10) {
            bindingResult.rejectValue("score", "error.review", "Score must be between 1 and 10.");
            hasErrors = true;
        }
        if (reviewForm.getComments() == null || reviewForm.getComments().trim().isEmpty()) {
            bindingResult.rejectValue("comments", "error.review", "Review comments are required.");
            hasErrors = true;
        }
        if (reviewForm.getRecommendation() == null || reviewForm.getRecommendation().trim().isEmpty()) {
            bindingResult.rejectValue("recommendation", "error.review", "Recommendation decision is required.");
            hasErrors = true;
        }

        if (hasErrors || bindingResult.hasErrors()) {
            reviewForm.setPaper(existingReview.getPaper());
            reviewForm.setReviewer(existingReview.getReviewer());
            model.addAttribute("review", reviewForm);
            return "reviewer/review-form";
        }

        reviewService.submitReview(reviewId, reviewForm.getScore(), reviewForm.getComments(), reviewForm.getRecommendation());
        return "redirect:/reviewer/papers?reviewed=true";
    }

    @GetMapping("/review-detail/{id}")
    public String viewReviewDetail(@PathVariable("id") Long reviewId, HttpSession session, Model model) {
        User reviewer = getLoggedUser(session);
        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getReviewer().getId().equals(reviewer.getId())) {
            return "redirect:/reviewer/papers?error=unauthorized";
        }

        model.addAttribute("review", review);
        return "reviewer/review-detail";
    }
}
