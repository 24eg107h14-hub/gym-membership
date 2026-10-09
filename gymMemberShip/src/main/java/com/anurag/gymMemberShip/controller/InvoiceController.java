package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.InvoiceResponseDto;
import com.anurag.gymMemberShip.service.InvoiceService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:5173")
public class InvoiceController {

      @Autowired
      private InvoiceService invoiceService;

      @GetMapping
      public ResponseEntity<List<InvoiceResponseDto>> getAll() {
            return ResponseEntity.ok(invoiceService.getAll());
      }

      @GetMapping("/my-invoices")
      public ResponseEntity<List<InvoiceResponseDto>> getMyInvoices() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(invoiceService.getMyInvoices(email));
      }

      @GetMapping("/{id}")
      public ResponseEntity<InvoiceResponseDto> getById(@PathVariable Long id) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            return ResponseEntity.ok(invoiceService.getById(id, email, isAdmin));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            invoiceService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Invoice deleted successfully"));
      }
}
