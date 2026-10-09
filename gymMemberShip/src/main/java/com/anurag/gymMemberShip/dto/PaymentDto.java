package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.PaymentMethod;
import com.anurag.gymMemberShip.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {

      private Long id;

      @NotNull(message = "Member ID is required")
      private Long memberId;

      private Long membershipId;

      @NotNull(message = "Amount is required")
      private BigDecimal amount;

      @NotNull(message = "Payment method is required")
      private PaymentMethod paymentMethod;

      private PaymentStatus paymentStatus;
      private String transactionId;
      private LocalDateTime paymentDate;

      public PaymentDto() {
      }

      public PaymentDto(Long id, Long memberId, Long membershipId, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String transactionId, LocalDateTime paymentDate) {
            this.id = id;
            this.memberId = memberId;
            this.membershipId = membershipId;
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

      public Long getMembershipId() {
            return membershipId;
      }

      public void setMembershipId(Long membershipId) {
            this.membershipId = membershipId;
      }

      public BigDecimal getAmount() {
            return amount;
      }

      public void setAmount(BigDecimal amount) {
            this.amount = amount;
      }

      public PaymentMethod getPaymentMethod() {
            return paymentMethod;
      }

      public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
      }

      public PaymentStatus getPaymentStatus() {
            return paymentStatus;
      }

      public void setPaymentStatus(PaymentStatus paymentStatus) {
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
