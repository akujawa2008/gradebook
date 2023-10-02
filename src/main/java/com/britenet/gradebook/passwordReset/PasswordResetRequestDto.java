package com.britenet.gradebook.passwordReset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class PasswordResetRequestDto {

    private String token;
    private String newPassword;

}