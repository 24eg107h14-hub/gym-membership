package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.LoginRequestDto;
import com.anurag.gymMemberShip.dto.LoginResponseDto;
import com.anurag.gymMemberShip.dto.RegisterRequestDto;
import com.anurag.gymMemberShip.service.AuthService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

      @Autowired
      private AuthService authService;

      @PostMapping("/register")
      public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDto dto) {
            authService.register(dto);
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
      }

      @PostMapping("/login")
      public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
            LoginResponseDto response = authService.login(dto);
            return ResponseEntity.ok(response);
      }
}
