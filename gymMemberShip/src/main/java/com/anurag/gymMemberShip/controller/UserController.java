package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.ChangePasswordDto;
import com.anurag.gymMemberShip.dto.MembershipResponseDto;
import com.anurag.gymMemberShip.dto.UserProfileDto;
import com.anurag.gymMemberShip.service.MemberService;
import com.anurag.gymMemberShip.service.MembershipService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

      @Autowired
      private MemberService memberService;

      @Autowired
      private MembershipService membershipService;

      @GetMapping("/profile")
      public ResponseEntity<UserProfileDto> getProfile() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserProfileDto profile = memberService.getProfileByEmail(email);
            return ResponseEntity.ok(profile);
      }

      @PutMapping("/profile")
      public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UserProfileDto dto) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserProfileDto updated = memberService.updateProfile(email, dto);
            return ResponseEntity.ok(updated);
      }

      @PostMapping("/change-password")
      public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            memberService.changePassword(email, dto);
            return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully"));
      }

      @GetMapping("/membership")
      public ResponseEntity<MembershipResponseDto> getMembership() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            MembershipResponseDto response = membershipService.getCurrentMembership(email);
            return ResponseEntity.ok(response);
      }
}
