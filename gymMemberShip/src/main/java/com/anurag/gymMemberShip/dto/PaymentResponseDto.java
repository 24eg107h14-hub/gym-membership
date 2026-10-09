package com.anurag.gymMemberShip.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDto {

      private Long id;
      private Long memberId;
      private String memberName;
      private String memberEmail;
      private Long membershipId;
      private String planName;
      private BigDecimal amount;
      private String paymentMethod;
      private String paymentStatus;
      private String transactionId;
      private LocalDateTime paymentDate;

      public PaymentResponseDto() {
      }

      public PaymentResponseDto(Long id, Long memberId, String memberName, String memberEmail, Long membershipId, String planName, BigDecimal amount, String paymentMethod, String paymentStatus, String transactionId, LocalDateTime paymentDate) {
            this.id = id;
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberEmail = memberEmail;
            this.membershipId = membershipId;
            this.planName = planName;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
            this.transactionId = transactionId;
            this.paymentDate = paymentDate;
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

      public Long getMembershipId() {
            return membershipId;
      }

      public void setMembershipId(Long membershipId) {
            this.membershipId = membershipId;
      }

      public String getPlanName() {
            return planName;
      }

      public void setPlanName(String planName) {
            this.planName = planName;
      }

      public BigDecimal getAmount() {
            return amount;
      }

      public void setAmount(BigDecimal amount) {
            this.amount = amount;
      }

      public String getPaymentMethod() {
            return paymentMethod;
      }

      public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
      }

      public String getPaymentStatus() {
            return paymentStatus;
      }

      public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
      }

      public String getTransactionId() {
            return transactionId;
      }

      public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
      }

      public LocalDateTime getPaymentDate() {
            return paymentDate;
      }

      public void setPaymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
      }
}
