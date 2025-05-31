package com.example.backend.service;

import com.example.backend.domain.common.LoginInfo;
import com.example.backend.domain.token.RefreshToken;
import com.example.backend.dto.login.JwtResponse;
import com.example.backend.provider.JwtTokenProvider;
import com.example.backend.repository.login.LoginInfoRepository;
import com.example.backend.repository.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    public JwtResponse login(String loginId, String password){
        System.out.println("loginId : " + loginId);
        System.out.println("password : " + password);
        System.out.println("passwordEncoder : " + passwordEncoder.encode(password));
        LoginInfo info = loginInfoRepository.findByLoginIdAndUseYn(loginId, "Y")
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));

        if(!passwordEncoder.matches(password, info.getLoginPw())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // Jwt에 loginType과 constarintKey포함
        String accessToken = jwtTokenProvider.createAccessToken(info.getLoginId(), info.getLoginType(), info.getConstraintKey());
        String refreshToken = jwtTokenProvider.createRefreshToken(info.getLoginId(), info.getLoginType(), info.getConstraintKey());

        RefreshToken refreshEntity = refreshTokenRepository.findByLoginId(loginId)
                .orElse(RefreshToken.builder()
                        .loginId(loginId)
                        .build()
                );
        refreshEntity.setRefreshToken(refreshToken);
        refreshEntity.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshEntity);

        return new JwtResponse(accessToken, refreshToken, info.getLoginType(), info.getConstraintKey());
    }
}
