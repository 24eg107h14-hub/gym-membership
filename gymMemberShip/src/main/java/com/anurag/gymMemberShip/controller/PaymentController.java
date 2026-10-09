package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.PaymentDto;
import com.anurag.gymMemberShip.dto.PaymentResponseDto;
import com.anurag.gymMemberShip.service.PaymentService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

      @Autowired
      private PaymentService paymentService;

      @GetMapping
      public ResponseEntity<List<PaymentResponseDto>> getAll() {
            return ResponseEntity.ok(paymentService.getAll());
      }

      @GetMapping("/my-payments")
      public ResponseEntity<List<PaymentResponseDto>> getMyPayments() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(paymentService.getMyPayments(email));
      }

      @GetMapping("/{id}")
      public ResponseEntity<PaymentResponseDto> getById(@PathVariable Long id) {
            return ResponseEntity.ok(paymentService.getById(id));
      }

      @PutMapping("/{id}")
      public ResponseEntity<PaymentResponseDto> update(@PathVariable Long id, @Valid @RequestBody PaymentDto dto) {
            return ResponseEntity.ok(paymentService.update(id, dto));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            paymentService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Payment deleted successfully"));
      }
}
