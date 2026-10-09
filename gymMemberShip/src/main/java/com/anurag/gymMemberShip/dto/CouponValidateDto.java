package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CouponValidateDto {

      public static class Request {
            @NotBlank(message = "Coupon code is required")
            private String code;

            @NotNull(message = "Plan price is required")
            private BigDecimal planPrice;

            public Request() {
            }

            public Request(String code, BigDecimal planPrice) {
                  this.code = code;
                  this.planPrice = planPrice;
            }

            public String getCode() {
                  return code;
            }

            public void setCode(String code) {
                  this.code = code;
            }

            public BigDecimal getPlanPrice() {
                  return planPrice;
            }

            public void setPlanPrice(BigDecimal planPrice) {
                  this.planPrice = planPrice;
            }
      }

      public static class Response {
            private boolean valid;
            private String message;
            private String code;
            private DiscountType discountType;
            private BigDecimal discountValue;
            private BigDecimal discountAmount;
            private BigDecimal finalAmount;

            public Response() {
            }

            public Response(boolean valid, String message, String code, DiscountType discountType, BigDecimal discountValue, BigDecimal discountAmount, BigDecimal finalAmount) {
                  this.valid = valid;
                  this.message = message;
                  this.code = code;
                  this.discountType = discountType;
                  this.discountValue = discountValue;
                  this.discountAmount = discountAmount;
                  this.finalAmount = finalAmount;
            }

            public boolean isValid() {
                  return valid;
            }

            public void setValid(boolean valid) {
                  this.valid = valid;
            }

            public String getMessage() {
                  return message;
            }

            public void setMessage(String message) {
                  this.message = message;
            }

            public String getCode() {
                  return code;
            }

            public void setCode(String code) {
                  this.code = code;
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

            public BigDecimal getDiscountAmount() {
                  return discountAmount;
            }

            public void setDiscountAmount(BigDecimal discountAmount) {
                  this.discountAmount = discountAmount;
            }

            public BigDecimal getFinalAmount() {
                  return finalAmount;
            }

            public void setFinalAmount(BigDecimal finalAmount) {
                  this.finalAmount = finalAmount;
            }
      }
}
