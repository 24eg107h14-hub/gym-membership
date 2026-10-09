package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.MembershipPurchaseDto;
import com.anurag.gymMemberShip.dto.MembershipResponseDto;
import com.anurag.gymMemberShip.service.MembershipService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberships")
@CrossOrigin(origins = "http://localhost:5173")
public class MembershipController {

      @Autowired
      private MembershipService membershipService;

      @PostMapping("/purchase")
      public ResponseEntity<MembershipResponseDto> purchase(@Valid @RequestBody MembershipPurchaseDto dto) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            MembershipResponseDto response = membershipService.purchase(dto, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
      }

      @GetMapping("/my-membership")
      public ResponseEntity<MembershipResponseDto> getMyMembership() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            MembershipResponseDto response = membershipService.getCurrentMembership(email);
            return ResponseEntity.ok(response);
      }

      @PostMapping("/{id}/cancel")
      public ResponseEntity<MembershipResponseDto> cancel(@PathVariable Long id) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(membershipService.cancel(id, email, isAdmin));
      }

      @PostMapping("/{id}/freeze")
      public ResponseEntity<MembershipResponseDto> freeze(@PathVariable Long id) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(membershipService.freeze(id, email, isAdmin));
      }

      @PostMapping("/{id}/renew")
      public ResponseEntity<MembershipResponseDto> renew(
                  @PathVariable Long id,
                  @RequestParam(required = false) Long planId) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(membershipService.renew(id, planId, email, isAdmin));
      }

      @PostMapping("/{id}/upgrade")
      public ResponseEntity<MembershipResponseDto> upgrade(
                  @PathVariable Long id,
                  @RequestParam Long newPlanId) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(membershipService.upgrade(id, newPlanId, email, isAdmin));
      }

      @GetMapping
      public ResponseEntity<List<MembershipResponseDto>> getAll() {
            return ResponseEntity.ok(membershipService.getAllMemberships());
      }
}
