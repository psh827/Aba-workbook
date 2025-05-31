package com.example.backend.controller.login;

import com.example.backend.domain.token.RefreshToken;
import com.example.backend.dto.token.TokenRequest;
import com.example.backend.dto.token.TokenResponse;
import com.example.backend.provider.JwtTokenProvider;
import com.example.backend.repository.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtProvider;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequest request) {
        String loginId = request.getLoginId();
        String requestRefreshToken = request.getRefreshToken();
        String loginType = request.getLoginType();
        String constraintKey = request.getConstraintKey();


        RefreshToken storedToken = refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Refresh Token not found"));

        if (!storedToken.getRefreshToken().equals(requestRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token expired");
        }

        // 새 토큰 발급
        String newAccessToken = jwtProvider.createAccessToken(loginId, loginType, constraintKey);
        String newRefreshToken = jwtProvider.createRefreshToken(loginId, loginType, constraintKey);

        // DB 갱신
        storedToken.setRefreshToken(newRefreshToken);
        storedToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(storedToken);

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }
}
