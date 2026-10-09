package com.anurag.gymMemberShip.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {

      private long totalMembers;
      private long activeMemberships;
      private long expiringMemberships;
      private long expiredMemberships;

      private long totalPlans;
      private long activePlans;

      private long totalPayments;
      private long paidPayments;
      private BigDecimal totalRevenue;

      private long totalCoupons;
      private long activeCoupons;

      private long totalBranches;
      private long activeBranches;

      private long totalTickets;
      private long openTickets;

      public DashboardStatsDto() {
      }

      public DashboardStatsDto(long totalMembers, long activeMemberships, long expiringMemberships, long expiredMemberships, long totalPlans, long activePlans, long totalPayments, long paidPayments, BigDecimal totalRevenue, long totalCoupons, long activeCoupons, long totalBranches, long activeBranches, long totalTickets, long openTickets) {
            this.totalMembers = totalMembers;
            this.activeMemberships = activeMemberships;
            this.expiringMemberships = expiringMemberships;
            this.expiredMemberships = expiredMemberships;
            this.totalPlans = totalPlans;
            this.activePlans = activePlans;
            this.totalPayments = totalPayments;
            this.paidPayments = paidPayments;
            this.totalRevenue = totalRevenue;
            this.totalCoupons = totalCoupons;
            this.activeCoupons = activeCoupons;
            this.totalBranches = totalBranches;
            this.activeBranches = activeBranches;
            this.totalTickets = totalTickets;
            this.openTickets = openTickets;
      }

      public long getTotalMembers() {
            return totalMembers;
      }

      public void setTotalMembers(long totalMembers) {
            this.totalMembers = totalMembers;
      }

      public long getActiveMemberships() {
            return activeMemberships;
      }

      public void setActiveMemberships(long activeMemberships) {
            this.activeMemberships = activeMemberships;
      }

      public long getExpiringMemberships() {
            return expiringMemberships;
      }

      public void setExpiringMemberships(long expiringMemberships) {
            this.expiringMemberships = expiringMemberships;
      }

      public long getExpiredMemberships() {
            return expiredMemberships;
      }

      public void setExpiredMemberships(long expiredMemberships) {
            this.expiredMemberships = expiredMemberships;
      }

      public long getTotalPlans() {
            return totalPlans;
      }

      public void setTotalPlans(long totalPlans) {
            this.totalPlans = totalPlans;
      }

      public long getActivePlans() {
            return activePlans;
      }

      public void setActivePlans(long activePlans) {
            this.activePlans = activePlans;
      }

      public long getTotalPayments() {
            return totalPayments;
      }

      public void setTotalPayments(long totalPayments) {
            this.totalPayments = totalPayments;
      }

      public long getPaidPayments() {
            return paidPayments;
      }

      public void setPaidPayments(long paidPayments) {
            this.paidPayments = paidPayments;
      }

      public BigDecimal getTotalRevenue() {
            return totalRevenue;
      }

      public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
      }

      public long getTotalCoupons() {
            return totalCoupons;
      }

      public void setTotalCoupons(long totalCoupons) {
            this.totalCoupons = totalCoupons;
      }

      public long getActiveCoupons() {
            return activeCoupons;
      }

      public void setActiveCoupons(long activeCoupons) {
            this.activeCoupons = activeCoupons;
      }

      public long getTotalBranches() {
            return totalBranches;
      }

      public void setTotalBranches(long totalBranches) {
            this.totalBranches = totalBranches;
      }

      public long getActiveBranches() {
            return activeBranches;
      }

      public void setActiveBranches(long activeBranches) {
            this.activeBranches = activeBranches;
      }

      public long getTotalTickets() {
            return totalTickets;
      }

      public void setTotalTickets(long totalTickets) {
            this.totalTickets = totalTickets;
      }

      public long getOpenTickets() {
            return openTickets;
      }

      public void setOpenTickets(long openTickets) {
            this.openTickets = openTickets;
      }
}
