package com.britenet.gradebook.passwordReset;

import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class PasswordResetControllerTest {

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordResetService resetService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void requestPasswordResetShouldReturnOk() {
        String email = "test@example.com";
        Users user = new Users();

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));
        ResponseEntity<Void> response = passwordResetController.requestPasswordReset(email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void requestPasswordResetShouldReturnNotFoundWhenUserNotExists() {
        String email = "test@example.com";

        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = passwordResetController.requestPasswordReset(email);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void resetPasswordShouldReturnOk() {
        PasswordResetRequestDto dto = new PasswordResetRequestDto();
        ResponseEntity<Void> response = passwordResetController.resetPassword(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}