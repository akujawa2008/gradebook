package com.britenet.gradebook.passwordReset;

import com.britenet.gradebook.users.UsersRepository;
import com.britenet.gradebook.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UsersRepository usersRepository;

    private final PasswordResetService resetService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody String email) {
        Users user = usersRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String token = resetService.generateToken();
        resetService.createPasswordResetTokenForUser(user, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequestDto request) {
        resetService.resetPassword(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
