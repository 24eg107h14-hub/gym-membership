package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.PlanDto;
import com.anurag.gymMemberShip.service.PlanService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@CrossOrigin(origins = "http://localhost:5173")
public class PlanController {

      @Autowired
      private PlanService planService;

      @GetMapping
      public ResponseEntity<List<PlanDto>> getAll(@RequestParam(required = false, defaultValue = "false") boolean all) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin && all) {
                  return ResponseEntity.ok(planService.getAll());
            } else if (isAdmin) {
                  return ResponseEntity.ok(planService.getAll());
            } else {
                  return ResponseEntity.ok(planService.getActivePlans());
            }
      }

      @GetMapping("/{id}")
      public ResponseEntity<PlanDto> getById(@PathVariable Long id) {
            return ResponseEntity.ok(planService.getById(id));
      }

      @PostMapping
      public ResponseEntity<PlanDto> add(@Valid @RequestBody PlanDto dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(planService.add(dto));
      }

      @PutMapping("/{id}")
      public ResponseEntity<PlanDto> update(@PathVariable Long id, @Valid @RequestBody PlanDto dto) {
            return ResponseEntity.ok(planService.update(id, dto));
      }

      @PatchMapping("/{id}/status")
      public ResponseEntity<PlanDto> toggleStatus(@PathVariable Long id) {
            return ResponseEntity.ok(planService.toggleStatus(id));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
            planService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Plan deleted successfully"));
      }
}
