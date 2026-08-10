package com.rcms.service;

import com.rcms.dto.RegisterDto;
import com.rcms.entity.User;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Dr. Alice Smith");
        sampleUser.setEmail("alice@university.edu");
        sampleUser.setPassword("securePassword123");
        sampleUser.setRole("AUTHOR");
        sampleUser.setInstitution("MIT");
    }

    @Test
    @DisplayName("Authenticate User - Success")
    void testAuthenticateSuccess() {
        when(userRepository.findByEmail("alice@university.edu")).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.authenticate("alice@university.edu", "securePassword123");

        assertTrue(result.isPresent());
        assertEquals("Dr. Alice Smith", result.get().getName());
    }

    @Test
    @DisplayName("Authenticate User - Wrong Password Returns Empty")
    void testAuthenticateWrongPassword() {
        when(userRepository.findByEmail("alice@university.edu")).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.authenticate("alice@university.edu", "wrongPassword");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Register Author - Success")
    void testRegisterAuthorSuccess() {
        RegisterDto dto = new RegisterDto();
        dto.setName("Bob Johnson");
        dto.setEmail("bob@stanford.edu");
        dto.setPassword("pass123456");

        when(userRepository.existsByEmail("bob@stanford.edu")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User saved = i.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        User registered = userService.registerAuthor(dto);

        assertNotNull(registered);
        assertEquals("AUTHOR", registered.getRole());
        assertEquals("bob@stanford.edu", registered.getEmail());
    }

    @Test
    @DisplayName("Register Author - Duplicate Email Throws Exception")
    void testRegisterAuthorDuplicateEmail() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("alice@university.edu");

        when(userRepository.existsByEmail("alice@university.edu")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerAuthor(dto));
    }

    @Test
    @DisplayName("Update Role - Success")
    void testUpdateRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateRole(1L, "REVIEWER");

        assertEquals("REVIEWER", updated.getRole());
    }
}
