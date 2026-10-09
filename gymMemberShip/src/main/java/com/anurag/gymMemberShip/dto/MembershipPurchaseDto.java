package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MembershipPurchaseDto {

      @NotNull(message = "Plan ID is required")
      private Long planId;

      private Long branchId;

      private String couponCode;

      private PaymentMethod paymentMethod = PaymentMethod.UPI;

      private LocalDate startDate;

      public MembershipPurchaseDto() {
      }

      public MembershipPurchaseDto(Long planId, Long branchId, String couponCode, PaymentMethod paymentMethod, LocalDate startDate) {
            this.planId = planId;
            this.branchId = branchId;
            this.couponCode = couponCode;
            this.paymentMethod = paymentMethod != null ? paymentMethod : PaymentMethod.UPI;
            this.startDate = startDate;
      }

      public Long getPlanId() {
            return planId;
      }

      public void setPlanId(Long planId) {
            this.planId = planId;
      }

      public Long getBranchId() {
            return branchId;
      }

      public void setBranchId(Long branchId) {
            this.branchId = branchId;
      }

      public String getCouponCode() {
            return couponCode;
      }

      public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
      }

      public PaymentMethod getPaymentMethod() {
            return paymentMethod;
      }

      public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
      }

      public LocalDate getStartDate() {
            return startDate;
      }

      public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
      }
}
