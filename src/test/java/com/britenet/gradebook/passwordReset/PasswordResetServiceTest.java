package com.britenet.gradebook.passwordReset;

import com.britenet.gradebook.users.Users;
import com.britenet.gradebook.users.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPasswordResetTokenForUser_ShouldReturnToken() {
        Users user = new Users();
        String token = "testToken";
        PasswordResetToken resetToken = new PasswordResetToken();

        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);

        PasswordResetToken result = passwordResetService.createPasswordResetTokenForUser(user, token);

        assertEquals(resetToken, result);
    }

    @Test
    public void getUserByToken_ShouldReturnUser() {
        String token = "testToken";
        PasswordResetToken resetToken = new PasswordResetToken();
        Users user = new Users();
        resetToken.setUser(user);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        Optional<Users> result = passwordResetService.getUserByToken(token);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void resetPassword_ShouldUpdateUserPassword() {
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        Users user = new Users();

        PasswordResetToken passwordResetToken = new PasswordResetToken(1L, "token", user);

        PasswordResetRequestDto request = PasswordResetRequestDto.builder()
                .token("token")
                .newPassword(newPassword)
                .build();


        when(tokenRepository.findByToken(any())).thenReturn(Optional.of(passwordResetToken));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        passwordResetService.resetPassword(request);

        assertEquals(encodedPassword, user.getPassword());
    }

}