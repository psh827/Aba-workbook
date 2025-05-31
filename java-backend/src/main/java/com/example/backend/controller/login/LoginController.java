package com.example.backend.controller.login;

import com.example.backend.dto.login.JwtResponse;
import com.example.backend.dto.login.LoginRequest;
import com.example.backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){

        String loginId = request.getLoginId();
        String password = request.getPassword();

        JwtResponse jwtResponse = loginService.login(loginId, password);
        return ResponseEntity.ok(jwtResponse);
    }
}
