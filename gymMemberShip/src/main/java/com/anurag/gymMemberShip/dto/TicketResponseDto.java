package com.anurag.gymMemberShip.dto;

import java.time.LocalDateTime;

public class TicketResponseDto {

      private Long id;
      private Long memberId;
      private String memberName;
      private String memberEmail;
      private String subject;
      private String description;
      private String category;
      private String status;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
      private String response;

      public TicketResponseDto() {
      }

      public TicketResponseDto(Long id, Long memberId, String memberName, String memberEmail, String subject, String description, String category, String status, LocalDateTime createdAt, LocalDateTime updatedAt, String response) {
            this.id = id;
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberEmail = memberEmail;
            this.subject = subject;
            this.description = description;
            this.category = category;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.response = response;
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

      public String getCategory() {
            return category;
      }

      public void setCategory(String category) {
            this.category = category;
      }

      public String getStatus() {
            return status;
      }

      public void setStatus(String status) {
            this.status = status;
      }

      public LocalDateTime getCreatedAt() {
            return createdAt;
      }

      public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
      }

      public LocalDateTime getUpdatedAt() {
            return updatedAt;
      }

      public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
      }

      public String getResponse() {
            return response;
      }

      public void setResponse(String response) {
            this.response = response;
      }
}
