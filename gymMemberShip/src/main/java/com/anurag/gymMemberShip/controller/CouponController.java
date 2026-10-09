package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.CouponDto;
import com.anurag.gymMemberShip.dto.CouponValidateDto;
import com.anurag.gymMemberShip.service.CouponService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:5173")
public class CouponController {

      @Autowired
      private CouponService couponService;

      @GetMapping
      public ResponseEntity<List<CouponDto>> getAll() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                  return ResponseEntity.ok(couponService.getAll());
            } else {
                  return ResponseEntity.ok(couponService.getActiveCoupons());
            }
      }

      @GetMapping("/{id}")
      public ResponseEntity<CouponDto> getById(@PathVariable Long id) {
            return ResponseEntity.ok(couponService.getById(id));
      }

      @PostMapping
      public ResponseEntity<CouponDto> create(@Valid @RequestBody CouponDto dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(couponService.create(dto));
      }

      @PutMapping("/{id}")
      public ResponseEntity<CouponDto> update(@PathVariable Long id, @Valid @RequestBody CouponDto dto) {
            return ResponseEntity.ok(couponService.update(id, dto));
      }

      @PatchMapping("/{id}/status")
      public ResponseEntity<CouponDto> toggleStatus(@PathVariable Long id) {
            return ResponseEntity.ok(couponService.toggleStatus(id));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            couponService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Coupon deleted successfully"));
      }

      @PostMapping("/validate")
      public ResponseEntity<CouponValidateDto.Response> validate(@Valid @RequestBody CouponValidateDto.Request req) {
            return ResponseEntity.ok(couponService.validate(req));
      }
}
