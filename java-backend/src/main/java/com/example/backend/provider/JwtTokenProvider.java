package com.example.backend.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "super-secret-key-for-aba-system-2025@jwt!-dkssudgktpdyqksrkqtmqslekdjelRkwlwjrdjdigkfwlahfmrpTwlaksdlfekswjrrpTtmqslek";
    private final long accessTokenValidity = 15 * 60 * 1000L; // 15분
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L; // 7일

    public String createAccessToken(String loginId, String loginType, String constraintKey) {
        return Jwts.builder()
                .setSubject(loginId)
                .claim("loginType", loginType)
                .claim("constraintKey", constraintKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String loginId, String loginType, String constraintKey) {
        return Jwts.builder()
                .setSubject(loginId)
                .claim("loginType", loginType)
                .claim("constraintKey", constraintKey)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }


    public String getLoginId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignkey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getLoginType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignkey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("loginType", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignkey(secretKey))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⏰ JWT 만료됨: " + e.getMessage());
        }  catch (MalformedJwtException e) {
            System.out.println("❗ 잘못된 형식: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("🔴 기타 JWT 오류: " + e.getMessage());
        }
        return false;
    }

    private Key getSignkey(String secretKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
