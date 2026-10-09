package com.anurag.gymMemberShip.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MembershipResponseDto {

      private Long id;
      private Long memberId;
      private String memberName;
      private String memberEmail;
      private Long planId;
      private String planName;
      private Integer durationMonths;
      private Long branchId;
      private String branchName;
      private LocalDate startDate;
      private LocalDate endDate;
      private BigDecimal price;
      private BigDecimal discount;
      private BigDecimal finalAmount;
      private String status;
      private long daysRemaining;

      public MembershipResponseDto() {
      }

      public MembershipResponseDto(Long id, Long memberId, String memberName, String memberEmail, Long planId, String planName, Integer durationMonths, Long branchId, String branchName, LocalDate startDate, LocalDate endDate, BigDecimal price, BigDecimal discount, BigDecimal finalAmount, String status, long daysRemaining) {
            this.id = id;
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberEmail = memberEmail;
            this.planId = planId;
            this.planName = planName;
            this.durationMonths = durationMonths;
            this.branchId = branchId;
            this.branchName = branchName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
            this.discount = discount;
            this.finalAmount = finalAmount;
            this.status = status;
            this.daysRemaining = daysRemaining;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public Long getMemberId() {
            return memberId;
      }

      public void setMemberId(Long memberId) {
            this.memberId = memberId;
      }

      public String getMemberName() {
            return memberName;
      }

      public void setMemberName(String memberName) {
            this.memberName = memberName;
      }

      public String getMemberEmail() {
            return memberEmail;
      }

      public void setMemberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
      }

      public Long getPlanId() {
            return planId;
      }

      public void setPlanId(Long planId) {
            this.planId = planId;
      }

      public String getPlanName() {
            return planName;
      }

      public void setPlanName(String planName) {
            this.planName = planName;
      }

      public Integer getDurationMonths() {
            return durationMonths;
      }

      public void setDurationMonths(Integer durationMonths) {
            this.durationMonths = durationMonths;
      }

      public Long getBranchId() {
            return branchId;
      }

      public void setBranchId(Long branchId) {
            this.branchId = branchId;
      }

      public String getBranchName() {
            return branchName;
      }

      public void setBranchName(String branchName) {
            this.branchName = branchName;
      }

      public LocalDate getStartDate() {
            return startDate;
      }

      public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
      }

      public LocalDate getEndDate() {
            return endDate;
      }

      public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
      }

      public BigDecimal getPrice() {
            return price;
      }

      public void setPrice(BigDecimal price) {
            this.price = price;
      }

      public BigDecimal getDiscount() {
            return discount;
      }

      public void setDiscount(BigDecimal discount) {
            this.discount = discount;
      }

      public BigDecimal getFinalAmount() {
            return finalAmount;
      }

      public void setFinalAmount(BigDecimal finalAmount) {
            this.finalAmount = finalAmount;
      }

      public String getStatus() {
            return status;
      }

      public void setStatus(String status) {
            this.status = status;
      }

      public long getDaysRemaining() {
            return daysRemaining;
      }

      public void setDaysRemaining(long daysRemaining) {
            this.daysRemaining = daysRemaining;
      }
}
