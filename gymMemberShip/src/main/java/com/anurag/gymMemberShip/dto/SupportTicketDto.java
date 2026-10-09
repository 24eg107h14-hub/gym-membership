package com.anurag.gymMemberShip.dto;

import com.anurag.gymMemberShip.enums.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SupportTicketDto {

      private Long id;

      @NotBlank(message = "Subject is required")
      private String subject;

      @NotBlank(message = "Description is required")
      private String description;

      @NotNull(message = "Category is required")
      private TicketCategory category = TicketCategory.OTHER;

      public SupportTicketDto() {
      }

      public SupportTicketDto(Long id, String subject, String description, TicketCategory category) {
            this.id = id;
            this.subject = subject;
            this.description = description;
            this.category = category;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getSubject() {
            return subject;
      }

      public void setSubject(String subject) {
            this.subject = subject;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public TicketCategory getCategory() {
            return category;
      }

      public void setCategory(TicketCategory category) {
            this.category = category;
      }
}
