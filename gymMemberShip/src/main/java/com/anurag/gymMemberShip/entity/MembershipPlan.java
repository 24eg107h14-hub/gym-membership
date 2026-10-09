package com.anurag.gymMemberShip.entity;

import com.anurag.gymMemberShip.enums.PlanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(nullable = false)
      private String name;

      private Integer durationMonths;

      private BigDecimal price;

      private String description;

      private String benefits;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private PlanStatus status = PlanStatus.ACTIVE;

      public MembershipPlan() {
      }

      public MembershipPlan(Long id, String name, Integer durationMonths, BigDecimal price, String description, String benefits, PlanStatus status) {
            this.id = id;
            this.name = name;
            this.durationMonths = durationMonths;
            this.price = price;
            this.description = description;
            this.benefits = benefits;
            this.status = status != null ? status : PlanStatus.ACTIVE;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public Integer getDurationMonths() {
            return durationMonths;
      }

      public void setDurationMonths(Integer durationMonths) {
            this.durationMonths = durationMonths;
      }

      public BigDecimal getPrice() {
            return price;
      }

      public void setPrice(BigDecimal price) {
            this.price = price;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public String getBenefits() {
            return benefits;
      }

      public void setBenefits(String benefits) {
            this.benefits = benefits;
      }

      public PlanStatus getStatus() {
            return status;
      }

      public void setStatus(PlanStatus status) {
            this.status = status;
      }
}
