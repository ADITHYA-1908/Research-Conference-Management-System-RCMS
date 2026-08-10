package com.rcms.controller;

import com.rcms.entity.*;
import com.rcms.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class PublicController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        List<Conference> openConferences = conferenceService.findOpenConferences();
        model.addAttribute("openConferences", openConferences);
        model.addAttribute("totalConferences", conferenceService.findAll().size());
        model.addAttribute("openConferencesCount", openConferences.size());
        model.addAttribute("totalPapers", paperService.countAll());
        model.addAttribute("acceptedPapersCount", paperService.countByStatus("ACCEPTED"));
        model.addAttribute("totalAuthors", userService.findAllAuthors().size());
        model.addAttribute("totalReviewers", userService.findAllReviewers().size());
        model.addAttribute("totalCertificates", certificateService.findAll().size());
        return "index";
    }

    @GetMapping("/conferences")
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.findAll());
        return "conferences";
    }

    @GetMapping("/conferences/{id}")
    public String conferenceDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        Optional<Conference> conferenceOpt = conferenceService.findById(id);
        if (conferenceOpt.isEmpty()) {
            return "redirect:/conferences?error=not_found";
        }
        Conference conference = conferenceOpt.get();
        User currentUser = (User) session.getAttribute("user");
        boolean isRegistered = false;
        if (currentUser != null) {
            isRegistered = registrationService.isRegistered(currentUser.getId(), conference.getId());
        }

        model.addAttribute("conference", conference);
        model.addAttribute("tracks", trackService.findByConference(id));
        model.addAttribute("isRegistered", isRegistered);
        return "conference-detail";
    }

    @PostMapping("/conferences/{id}/register")
    public String registerParticipant(@PathVariable("id") Long conferenceId, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login?error=please_login";
        }
        try {
            Conference conference = conferenceService.findById(conferenceId)
                    .orElseThrow(() -> new IllegalArgumentException("Conference not found"));
            registrationService.registerUserForConference(currentUser, conference);
            return "redirect:/conferences/" + conferenceId + "?registered=true";
        } catch (IllegalArgumentException e) {
            return "redirect:/conferences/" + conferenceId + "?error=" + e.getMessage();
        }
    }

    // --- CERTIFICATE VERIFICATION & DOWNLOAD ---
    @GetMapping("/certificates/download/{id}")
    public String downloadCertificate(@PathVariable("id") Long id, Model model) {
        Certificate cert = certificateService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        model.addAttribute("cert", cert);
        return "certificate-view";
    }

    @GetMapping("/certificates/verify")
    public String showVerifyForm() {
        return "certificate-verify";
    }

    @GetMapping("/certificates/verify/{code}")
    public String verifyCertificateByCode(@PathVariable("code") String code, Model model) {
        Optional<Certificate> certOpt = certificateService.findByCode(code);
        model.addAttribute("searchedCode", code);
        if (certOpt.isPresent()) {
            model.addAttribute("cert", certOpt.get());
            model.addAttribute("isValid", true);
        } else {
            model.addAttribute("isValid", false);
            model.addAttribute("errorMessage", "No authentic certificate found with code: " + code);
        }
        return "certificate-verify";
    }

    @PostMapping("/certificates/verify")
    public String processVerifyCertificate(@RequestParam(value = "certificateCode", required = false) String code) {
        if (code == null || code.trim().isEmpty()) {
            return "redirect:/certificates/verify?error=empty_code";
        }
        return "redirect:/certificates/verify/" + code.trim().toUpperCase();
    }
}
