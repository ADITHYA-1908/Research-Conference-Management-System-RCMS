package com.rcms.service;

import com.rcms.entity.Track;
import com.rcms.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    public List<Track> findByConference(Long conferenceId) {
        return trackRepository.findByConferenceId(conferenceId);
    }

    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
    }

    public Track save(Track track) {
        return trackRepository.save(track);
    }

    public void deleteById(Long id) {
        trackRepository.deleteById(id);
    }
}
