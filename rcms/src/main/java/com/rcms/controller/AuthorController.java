package com.rcms.controller;

import com.rcms.entity.*;
import com.rcms.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private CertificateService certificateService;

    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = getLoggedUser(session);
        List<Paper> myPapers = paperService.findByAuthor(currentUser.getId());

        model.addAttribute("myPapers", myPapers);
        model.addAttribute("myCertificates", certificateService.findByUser(currentUser.getId()));
        model.addAttribute("totalSubmitted", myPapers.size());
        model.addAttribute("totalAccepted", myPapers.stream().filter(p -> "ACCEPTED".equalsIgnoreCase(p.getStatus())).count());
        model.addAttribute("totalUnderReview", myPapers.stream().filter(p -> "UNDER_REVIEW".equalsIgnoreCase(p.getStatus())).count());
        return "author/dashboard";
    }

    @GetMapping("/submit-paper")
    public String showSubmitForm(Model model) {
        List<Conference> conferences = conferenceService.findOpenConferences();
        model.addAttribute("paper", new Paper());
        model.addAttribute("conferences", conferences);
        model.addAttribute("tracks", trackService.findAll());
        return "author/submit-paper";
    }

    @PostMapping("/submit-paper")
    public String processSubmitPaper(@Valid @ModelAttribute("paper") Paper paper,
                                     BindingResult bindingResult,
                                     @RequestParam(value = "conferenceId", required = false) Long conferenceId,
                                     @RequestParam(value = "trackId", required = false) Long trackId,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     HttpSession session,
                                     Model model) {
        User author = getLoggedUser(session);

        if (conferenceId == null) {
            bindingResult.reject("conferenceId.required", "Please select a target conference.");
        }

        try {
            paperService.validatePaperFile(file);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("file.invalid", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("conferences", conferenceService.findOpenConferences());
            model.addAttribute("tracks", trackService.findAll());
            return "author/submit-paper";
        }

        try {
            Conference conference = conferenceService.findById(conferenceId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid conference selected."));
            if (trackId != null) {
                trackService.findById(trackId).ifPresent(paper::setTrack);
            }
            paperService.submitPaper(paper, file, author, conference);
            return "redirect:/author/papers?submitted=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to submit paper: " + e.getMessage());
            model.addAttribute("conferences", conferenceService.findOpenConferences());
            model.addAttribute("tracks", trackService.findAll());
            return "author/submit-paper";
        }
    }

    @GetMapping("/papers")
    public String listMyPapers(HttpSession session, Model model) {
        User author = getLoggedUser(session);
        List<Paper> papers = paperService.findByAuthor(author.getId());
        model.addAttribute("papers", papers);
        return "author/papers";
    }

    @GetMapping("/paper/{id}")
    public String viewPaperDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        User author = getLoggedUser(session);
        Paper paper = paperService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found"));

        if (!paper.getAuthor().getId().equals(author.getId())) {
            return "redirect:/author/papers?error=unauthorized";
        }

        List<Review> reviews = reviewService.findByPaper(id);
        model.addAttribute("paper", paper);
        model.addAttribute("reviews", reviews);
        return "author/paper-detail";
    }

    @PostMapping("/paper/{id}/withdraw")
    public String withdrawPaper(@PathVariable("id") Long id, HttpSession session) {
        User author = getLoggedUser(session);
        Paper paper = paperService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found"));

        if (paper.getAuthor().getId().equals(author.getId())) {
            paperService.updateStatus(id, "WITHDRAWN");
        }
        return "redirect:/author/paper/" + id + "?withdrawn=true";
    }

    @GetMapping("/profile")
    public String editProfile(HttpSession session, Model model) {
        User currentUser = getLoggedUser(session);
        User user = userService.findById(currentUser.getId()).orElse(currentUser);
        model.addAttribute("user", user);
        return "author/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("name") String name,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "institution", required = false) String institution,
                                @RequestParam(value = "bio", required = false) String bio,
                                HttpSession session, Model model) {
        User currentUser = getLoggedUser(session);
        User stub = new User();
        stub.setName(name); stub.setPhone(phone); stub.setInstitution(institution); stub.setBio(bio);
        User savedUser = userService.updateProfile(currentUser.getId(), stub);
        session.setAttribute("user", savedUser);
        model.addAttribute("successMessage", "Profile updated successfully!");
        model.addAttribute("user", savedUser);
        return "author/profile";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session,
                                 Model model) {
        User currentUser = getLoggedUser(session);
        try {
            userService.updatePassword(currentUser.getId(), currentPassword, newPassword);
            model.addAttribute("passwordSuccess", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("passwordError", e.getMessage());
        }
        model.addAttribute("user", userService.findById(currentUser.getId()).orElse(currentUser));
        return "author/profile";
    }
}
