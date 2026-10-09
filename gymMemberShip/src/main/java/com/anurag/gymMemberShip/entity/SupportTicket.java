package com.anurag.gymMemberShip.entity;

import com.anurag.gymMemberShip.enums.TicketCategory;
import com.anurag.gymMemberShip.enums.TicketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "member_id", nullable = false)
      private Member member;

      @Column(nullable = false)
      private String subject;

      @Column(nullable = false, length = 2000)
      private String description;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private TicketCategory category = TicketCategory.OTHER;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private TicketStatus status = TicketStatus.OPEN;

      @Column(nullable = false)
      private LocalDateTime createdAt = LocalDateTime.now();

      private LocalDateTime updatedAt;

      @Column(length = 2000)
      private String response;

      public SupportTicket() {
      }

      public SupportTicket(Long id, Member member, String subject, String description, TicketCategory category, TicketStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, String response) {
            this.id = id;
            this.member = member;
            this.subject = subject;
            this.description = description;
            this.category = category != null ? category : TicketCategory.OTHER;
            this.status = status != null ? status : TicketStatus.OPEN;
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
            this.updatedAt = updatedAt;
            this.response = response;
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public Member getMember() {
            return member;
      }

      public void setMember(Member member) {
            this.member = member;
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

      public TicketStatus getStatus() {
            return status;
      }

      public void setStatus(TicketStatus status) {
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
