package com.example.backend.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String loginType;
    private String constraintKey;
}
