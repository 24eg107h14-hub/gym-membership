package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.PlanStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PlanDto {

      private Long id;

      @NotBlank(message = "Plan name is required")
      private String name;

      @NotNull(message = "Duration in months is required")
      @Min(value = 1, message = "Duration must be at least 1 month")
      private Integer durationMonths;

      @NotNull(message = "Price is required")
      private BigDecimal price;

      private String description;

      private String benefits;

      private PlanStatus status = PlanStatus.ACTIVE;

      public PlanDto() {
      }

      public PlanDto(Long id, String name, Integer durationMonths, BigDecimal price, String description, String benefits, PlanStatus status) {
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
