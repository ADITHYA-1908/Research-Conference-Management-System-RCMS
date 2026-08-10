package com.rcms.service;

import com.rcms.entity.Conference;
import com.rcms.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    public List<Conference> findAll() {
        return conferenceRepository.findAllByOrderByStartDateDesc();
    }

    public List<Conference> findOpenConferences() {
        return conferenceRepository.findByStatus("OPEN");
    }

    public Optional<Conference> findById(Long id) {
        return conferenceRepository.findById(id);
    }

    public void validateConferenceDates(Conference conference) {
        if (conference.getStartDate() != null && conference.getEndDate() != null) {
            if (conference.getEndDate().isBefore(conference.getStartDate())) {
                throw new IllegalArgumentException("Conference End Date (" + conference.getEndDate() + ") cannot be before Start Date (" + conference.getStartDate() + ").");
            }
        }
        if (conference.getSubmissionDeadline() != null && conference.getStartDate() != null) {
            if (conference.getSubmissionDeadline().isAfter(conference.getStartDate())) {
                throw new IllegalArgumentException("Submission Deadline (" + conference.getSubmissionDeadline() + ") cannot be after Conference Start Date (" + conference.getStartDate() + ").");
            }
        }
    }

    public Conference save(Conference conference) {
        validateConferenceDates(conference);
        return conferenceRepository.save(conference);
    }

    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }
}
