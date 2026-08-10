package com.rcms.service;

import com.rcms.entity.Conference;
import com.rcms.entity.ConferenceRegistration;
import com.rcms.entity.User;
import com.rcms.repository.ConferenceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private ConferenceRegistrationRepository registrationRepository;

    public ConferenceRegistration registerUserForConference(User user, Conference conference) {
        if (registrationRepository.existsByUserIdAndConferenceId(user.getId(), conference.getId())) {
            throw new IllegalArgumentException("You are already registered for this conference.");
        }
        ConferenceRegistration reg = new ConferenceRegistration();
        reg.setUser(user);
        reg.setConference(conference);
        return registrationRepository.save(reg);
    }

    public boolean isRegistered(Long userId, Long conferenceId) {
        return registrationRepository.existsByUserIdAndConferenceId(userId, conferenceId);
    }

    public List<ConferenceRegistration> findByUser(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    public List<ConferenceRegistration> findByConference(Long conferenceId) {
        return registrationRepository.findByConferenceId(conferenceId);
    }
}
