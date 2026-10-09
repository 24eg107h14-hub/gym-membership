package com.anurag.gymMemberShip.entity;

import com.anurag.gymMemberShip.enums.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
public class Coupon {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(nullable = false, unique = true)
      private String code;

      private String description;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private DiscountType discountType = DiscountType.PERCENTAGE;

      @Column(nullable = false)
      private BigDecimal discountValue;

      @Column(nullable = false)
      private boolean active = true;

      private LocalDate startDate;

      private LocalDate endDate;

      public Coupon() {
      }

      public Coupon(Long id, String code, String description, DiscountType discountType, BigDecimal discountValue, boolean active, LocalDate startDate, LocalDate endDate) {
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
