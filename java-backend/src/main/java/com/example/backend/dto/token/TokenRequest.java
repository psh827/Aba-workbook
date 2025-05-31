package com.example.backend.dto.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    private String loginId;
    private String refreshToken;
    private String loginType;
    private String constraintKey;
}
