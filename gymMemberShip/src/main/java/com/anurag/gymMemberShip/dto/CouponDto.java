package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponDto {

      private Long id;

      @NotBlank(message = "Coupon code is required")
      private String code;

      private String description;

      @NotNull(message = "Discount type is required")
      private DiscountType discountType = DiscountType.PERCENTAGE;

      @NotNull(message = "Discount value is required")
      private BigDecimal discountValue;

      private boolean active = true;

      private LocalDate startDate;

      private LocalDate endDate;

      public CouponDto() {
      }

      public CouponDto(Long id, String code, String description, DiscountType discountType, BigDecimal discountValue, boolean active, LocalDate startDate, LocalDate endDate) {
            this.id = id;
            this.code = code;
            this.description = description;
            this.discountType = discountType;
            this.discountValue = discountValue;
            this.active = active;
            this.startDate = startDate;
            this.endDate = endDate;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getCode() {
            return code;
      }

      public void setCode(String code) {
            this.code = code;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public DiscountType getDiscountType() {
            return discountType;
      }

      public void setDiscountType(DiscountType discountType) {
            this.discountType = discountType;
      }

      public BigDecimal getDiscountValue() {
            return discountValue;
      }

      public void setDiscountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
      }

      public boolean isActive() {
            return active;
      }

      public void setActive(boolean active) {
            this.active = active;
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
}
