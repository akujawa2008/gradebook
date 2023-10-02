package com.britenet.gradebook.passwordReset;

import com.britenet.gradebook.exception.UserNotFoundException;
import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;

    public PasswordResetToken createPasswordResetTokenForUser(final Users user, final String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        return tokenRepository.save(myToken);
    }

    public Optional<Users> getUserByToken(final String token) {
        Optional<PasswordResetToken> passwordResetToken = tokenRepository.findByToken(token);
        return passwordResetToken
                .map(PasswordResetToken::getUser);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void resetPassword (PasswordResetRequestDto request) {
        Users user = getUserByToken(request.getToken())
                .orElseThrow(() -> new UserNotFoundException("No user not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }
}