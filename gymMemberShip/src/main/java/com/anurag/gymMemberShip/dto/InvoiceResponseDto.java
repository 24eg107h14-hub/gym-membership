package com.anurag.gymMemberShip.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceResponseDto {

      private Long id;
      private String invoiceNumber;
      private Long memberId;
      private String memberName;
      private String memberEmail;
      private String memberPhone;
      private String memberAddress;
      private Long membershipId;
      private String planName;
      private String branchName;
      private Long paymentId;
      private String paymentMethod;
      private String transactionId;
      private BigDecimal amount;
      private BigDecimal discount;
      private BigDecimal finalAmount;
      private LocalDateTime invoiceDate;

      public InvoiceResponseDto() {
      }

      public InvoiceResponseDto(Long id, String invoiceNumber, Long memberId, String memberName, String memberEmail, String memberPhone, String memberAddress, Long membershipId, String planName, String branchName, Long paymentId, String paymentMethod, String transactionId, BigDecimal amount, BigDecimal discount, BigDecimal finalAmount, LocalDateTime invoiceDate) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberEmail = memberEmail;
            this.memberPhone = memberPhone;
            this.memberAddress = memberAddress;
            this.membershipId = membershipId;
            this.planName = planName;
            this.branchName = branchName;
            this.paymentId = paymentId;
            this.paymentMethod = paymentMethod;
            this.transactionId = transactionId;
            this.amount = amount;
            this.discount = discount;
            this.finalAmount = finalAmount;
            this.invoiceDate = invoiceDate;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getInvoiceNumber() {
            return invoiceNumber;
      }

      public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
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

      public String getMemberPhone() {
            return memberPhone;
      }

      public void setMemberPhone(String memberPhone) {
            this.memberPhone = memberPhone;
      }

      public String getMemberAddress() {
            return memberAddress;
      }

      public void setMemberAddress(String memberAddress) {
            this.memberAddress = memberAddress;
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

      public String getBranchName() {
            return branchName;
      }

      public void setBranchName(String branchName) {
            this.branchName = branchName;
      }

      public Long getPaymentId() {
            return paymentId;
      }

      public void setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
      }

      public String getPaymentMethod() {
            return paymentMethod;
      }

      public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
      }

      public String getTransactionId() {
            return transactionId;
      }

      public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
      }

      public BigDecimal getAmount() {
            return amount;
      }

      public void setAmount(BigDecimal amount) {
            this.amount = amount;
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

      public LocalDateTime getInvoiceDate() {
            return invoiceDate;
      }

      public void setInvoiceDate(LocalDateTime invoiceDate) {
            this.invoiceDate = invoiceDate;
      }
}
