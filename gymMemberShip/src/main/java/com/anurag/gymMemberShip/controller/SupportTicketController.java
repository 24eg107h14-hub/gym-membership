package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.SupportTicketDto;
import com.anurag.gymMemberShip.dto.TicketResponseDto;
import com.anurag.gymMemberShip.enums.TicketCategory;
import com.anurag.gymMemberShip.enums.TicketStatus;
import com.anurag.gymMemberShip.service.SupportTicketService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class SupportTicketController {

      @Autowired
      private SupportTicketService ticketService;

      @PostMapping
      public ResponseEntity<TicketResponseDto> create(@Valid @RequestBody SupportTicketDto dto) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(dto, email));
      }

      @GetMapping("/my-tickets")
      public ResponseEntity<List<TicketResponseDto>> getMyTickets() {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(ticketService.getMyTickets(email));
      }

      @GetMapping("/{id}")
      public ResponseEntity<TicketResponseDto> getById(@PathVariable Long id) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdminOrSupport = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPPORT"));

            return ResponseEntity.ok(ticketService.getById(id, email, isAdminOrSupport));
      }

      @GetMapping
      public ResponseEntity<List<TicketResponseDto>> getAll(
                  @RequestParam(required = false) TicketStatus status,
                  @RequestParam(required = false) TicketCategory category) {
            return ResponseEntity.ok(ticketService.getAll(status, category));
      }

      @PatchMapping("/{id}/status")
      public ResponseEntity<TicketResponseDto> updateStatus(
                  @PathVariable Long id,
                  @RequestParam TicketStatus status) {
            return ResponseEntity.ok(ticketService.updateStatus(id, status));
      }

      @PostMapping("/{id}/respond")
      public ResponseEntity<TicketResponseDto> respond(
                  @PathVariable Long id,
                  @RequestBody Map<String, String> body) {
            String responseText = body.getOrDefault("response", "");
            return ResponseEntity.ok(ticketService.respond(id, responseText));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            ticketService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Ticket deleted successfully"));
      }
}
