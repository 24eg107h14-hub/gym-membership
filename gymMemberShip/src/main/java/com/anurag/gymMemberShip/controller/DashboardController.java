package com.anurag.gymMemberShip.controller;

import com.anurag.gymMemberShip.dto.DashboardStatsDto;
import com.anurag.gymMemberShip.dto.SupportDashboardStatsDto;
import com.anurag.gymMemberShip.service.DashboardService;
import com.anurag.gymMemberShip.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

      @Autowired
      private DashboardService dashboardService;

      @Autowired
      private SupportTicketService supportTicketService;

      @GetMapping("/stats")
      public ResponseEntity<DashboardStatsDto> getStats() {
            return ResponseEntity.ok(dashboardService.getStats());
      }

      @GetMapping("/support-stats")
      public ResponseEntity<SupportDashboardStatsDto> getSupportStats() {
            return ResponseEntity.ok(supportTicketService.getSupportDashboardStats());
      }
}
