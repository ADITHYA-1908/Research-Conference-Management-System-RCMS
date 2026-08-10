package com.rcms.service;

import com.rcms.entity.Conference;
import com.rcms.repository.ConferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {

    @Mock
    private ConferenceRepository conferenceRepository;

    @InjectMocks
    private ConferenceService conferenceService;

    private Conference validConf;

    @BeforeEach
    void setUp() {
        validConf = new Conference();
        validConf.setId(100L);
        validConf.setTitle("IEEE International AI & Computing 2026");
        validConf.setStartDate(LocalDate.of(2026, 10, 15));
        validConf.setEndDate(LocalDate.of(2026, 10, 18));
        validConf.setSubmissionDeadline(LocalDate.of(2026, 9, 30));
        validConf.setStatus("OPEN");
    }

    @Test
    @DisplayName("Save Conference - Valid Dates Success")
    void testSaveValidConference() {
        when(conferenceRepository.save(any(Conference.class))).thenReturn(validConf);

        Conference saved = conferenceService.save(validConf);

        assertNotNull(saved);
        assertEquals("IEEE International AI & Computing 2026", saved.getTitle());
        verify(conferenceRepository, times(1)).save(validConf);
    }

    @Test
    @DisplayName("Validate Dates - End Date Before Start Date Throws Exception")
    void testInvalidEndBeforeStartDate() {
        Conference invalid = new Conference();
        invalid.setStartDate(LocalDate.of(2026, 10, 20));
        invalid.setEndDate(LocalDate.of(2026, 10, 15)); // invalid!

        assertThrows(IllegalArgumentException.class, () -> conferenceService.validateConferenceDates(invalid));
    }

    @Test
    @DisplayName("Validate Dates - Deadline After Start Date Throws Exception")
    void testInvalidDeadlineAfterStartDate() {
        Conference invalid = new Conference();
        invalid.setStartDate(LocalDate.of(2026, 10, 15));
        invalid.setSubmissionDeadline(LocalDate.of(2026, 10, 20)); // invalid!

        assertThrows(IllegalArgumentException.class, () -> conferenceService.validateConferenceDates(invalid));
    }
}
