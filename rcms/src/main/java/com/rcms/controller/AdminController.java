package com.rcms.controller;

import com.rcms.entity.*;
import com.rcms.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalConferences", conferenceService.findAll().size());
        model.addAttribute("totalPapers", paperService.countAll());
        model.addAttribute("submittedPapers", paperService.countByStatus("SUBMITTED"));
        model.addAttribute("underReviewPapers", paperService.countByStatus("UNDER_REVIEW"));
        model.addAttribute("acceptedPapers", paperService.countByStatus("ACCEPTED"));
        model.addAttribute("rejectedPapers", paperService.countByStatus("REJECTED"));
        model.addAttribute("totalAuthors", userService.findAllAuthors().size());
        model.addAttribute("recentPapers", paperService.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/conferences")
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.findAll());
        return "admin/conferences";
    }

    @GetMapping("/conference/new")
    public String newConferenceForm(Model model) {
        model.addAttribute("conference", new Conference());
        return "admin/conference-form";
    }

    @GetMapping("/conference/edit/{id}")
    public String editConferenceForm(@PathVariable("id") Long id, Model model) {
        Optional<Conference> conferenceOpt = conferenceService.findById(id);
        if (conferenceOpt.isEmpty()) {
            return "redirect:/admin/conferences?error=not_found";
        }
        model.addAttribute("conference", conferenceOpt.get());
        return "admin/conference-form";
    }

    @PostMapping("/conference/save")
    public String saveConference(@Valid @ModelAttribute("conference") Conference conference,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/conference-form";
        }
        try {
            if (conference.getStatus() == null || conference.getStatus().isBlank()) {
                conference.setStatus("OPEN");
            }
            conferenceService.save(conference);
            return "redirect:/admin/conferences?success=true";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("date.invalid", e.getMessage());
            return "admin/conference-form";
        }
    }

    @PostMapping("/conference/delete/{id}")
    public String deleteConference(@PathVariable("id") Long id) {
        conferenceService.deleteById(id);
        return "redirect:/admin/conferences?deleted=true";
    }

    // --- TRACK MANAGEMENT ---
    @GetMapping("/tracks")
    public String listTracks(Model model) {
        model.addAttribute("tracks", trackService.findAll());
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("track", new Track());
        return "admin/tracks";
    }

    @PostMapping("/track/save")
    public String saveTrack(@Valid @ModelAttribute("track") Track track,
                            BindingResult bindingResult,
                            @RequestParam("conferenceId") Long conferenceId,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tracks", trackService.findAll());
            model.addAttribute("conferences", conferenceService.findAll());
            return "admin/tracks";
        }
        Conference conference = conferenceService.findById(conferenceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid conference ID"));
        track.setConference(conference);
        trackService.save(track);
        return "redirect:/admin/tracks?saved=true";
    }

    @PostMapping("/track/delete/{id}")
    public String deleteTrack(@PathVariable("id") Long id) {
        trackService.deleteById(id);
        return "redirect:/admin/tracks?deleted=true";
    }

    // --- USER MANAGEMENT ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", userService.findAllAuthors());
        return "admin/authors";
    }

    @PostMapping("/user/{id}/role")
    public String updateUserRole(@PathVariable("id") Long userId, @RequestParam("role") String role) {
        userService.updateRole(userId, role);
        return "redirect:/admin/users?updated=true";
    }

    // --- CERTIFICATE GENERATION ---
    @GetMapping("/certificates")
    public String listCertificates(Model model) {
        model.addAttribute("certificates", certificateService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("papers", paperService.findAll());
        return "admin/certificates";
    }

    @PostMapping("/certificate/issue")
    public String issueCertificate(@RequestParam("userId") Long userId,
                                   @RequestParam("conferenceId") Long conferenceId,
                                   @RequestParam(value = "paperId", required = false) Long paperId,
                                   @RequestParam("type") String type,
                                   Model model) {
        try {
            User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Conference conference = conferenceService.findById(conferenceId).orElseThrow(() -> new IllegalArgumentException("Conference not found"));
            Paper paper = (paperId != null) ? paperService.findById(paperId).orElse(null) : null;

            Certificate cert = certificateService.issueCertificate(user, conference, paper, type);
            notificationService.sendEmailNotification(user.getEmail(), "Certificate Issued!", 
                    "Your " + type + " certificate (Code: " + cert.getCertificateCode() + ") has been issued.");

            return "redirect:/admin/certificates?issued=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("certificates", certificateService.findAll());
            model.addAttribute("users", userService.findAll());
            model.addAttribute("conferences", conferenceService.findAll());
            model.addAttribute("papers", paperService.findAll());
            return "admin/certificates";
        }
    }

    // --- PAPERS & REVIEWS ---
    @GetMapping("/papers")
    public String listPapers(Model model) {
        model.addAttribute("papers", paperService.findAll());
        return "admin/papers";
    }

    @GetMapping("/paper/{id}")
    public String paperDetail(@PathVariable("id") Long id, Model model) {
        Paper paper = paperService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found"));
        List<User> reviewers = userService.findAllReviewers();
        List<Review> reviews = reviewService.findByPaper(id);

        model.addAttribute("paper", paper);
        model.addAttribute("reviewers", reviewers);
        model.addAttribute("reviews", reviews);
        return "admin/paper-detail";
    }

    @PostMapping("/paper/{id}/assign")
    public String assignReviewer(@PathVariable("id") Long paperId,
                                 @RequestParam("reviewerId") Long reviewerId) {
        try {
            Review review = reviewService.assignReviewer(paperId, reviewerId);
            notificationService.sendEmailNotification(review.getReviewer().getEmail(), 
                    "Paper Review Assigned", "You have been assigned to review paper ID: " + paperId);
            return "redirect:/admin/paper/" + paperId + "?assigned=true";
        } catch (Exception e) {
            return "redirect:/admin/paper/" + paperId + "?error=" + e.getMessage();
        }
    }

    @PostMapping("/paper/{id}/decision")
    public String paperDecision(@PathVariable("id") Long paperId,
                                @RequestParam("status") String status) {
        Paper paper = paperService.updateStatus(paperId, status);
        notificationService.sendEmailNotification(paper.getAuthor().getEmail(), 
                "Paper Status Decision Updated", "Your paper titled '" + paper.getTitle() + "' status is now: " + status);
        return "redirect:/admin/paper/" + paperId + "?decision=true";
    }

    @GetMapping("/reviews")
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviews";
    }
}
