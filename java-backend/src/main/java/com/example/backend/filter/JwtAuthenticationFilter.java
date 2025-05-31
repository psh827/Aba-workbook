package com.example.backend.filter;

import com.example.backend.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String token = resolveToken(request);

        System.out.println("🔥 token: " + token);
        System.out.println("🛡️ valid? " + jwtTokenProvider.validateToken(token));
        System.out.println("🛡️ SecurityContext 설정됨: " + SecurityContextHolder.getContext().getAuthentication());

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String loginId = jwtTokenProvider.getLoginId(token);
                String loginType = jwtTokenProvider.getLoginType(token);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + loginType);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(loginId, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else if (token != null) {
                // 토큰이 존재하지만 유효하지 않음 (예: 만료됨)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired or invalid");
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation failed");
            return;
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
