package com.britenet.gradebook.users;

import lombok.Data;

@Data
public class UsersRequestDto {

    private String username;
    private String password;
    private String email;
    private String role;
}
