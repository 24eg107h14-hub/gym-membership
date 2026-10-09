package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.BranchDto;
import com.anurag.gymMemberShip.service.BranchService;
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
@RequestMapping("/api/branches")
@CrossOrigin(origins = "http://localhost:5173")
public class BranchController {

      @Autowired
      private BranchService branchService;

      @GetMapping
      public ResponseEntity<List<BranchDto>> getAll() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                  return ResponseEntity.ok(branchService.getAll());
            } else {
                  return ResponseEntity.ok(branchService.getActiveBranches());
            }
      }

      @GetMapping("/{id}")
      public ResponseEntity<BranchDto> getById(@PathVariable Long id) {
            return ResponseEntity.ok(branchService.getById(id));
      }

      @PostMapping
      public ResponseEntity<BranchDto> create(@Valid @RequestBody BranchDto dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(branchService.create(dto));
      }

      @PutMapping("/{id}")
      public ResponseEntity<BranchDto> update(@PathVariable Long id, @Valid @RequestBody BranchDto dto) {
            return ResponseEntity.ok(branchService.update(id, dto));
      }

      @PatchMapping("/{id}/status")
      public ResponseEntity<BranchDto> toggleStatus(@PathVariable Long id) {
            return ResponseEntity.ok(branchService.toggleStatus(id));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            branchService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Branch deleted successfully"));
      }
}
