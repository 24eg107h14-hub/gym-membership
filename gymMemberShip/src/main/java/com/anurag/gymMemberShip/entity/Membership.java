package com.anurag.gymMemberShip.entity;

import com.anurag.gymMemberShip.enums.MembershipStatus;
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
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "memberships")
public class Membership {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "member_id", nullable = false)
      private Member member;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "plan_id", nullable = false)
      private MembershipPlan plan;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "branch_id")
      private Branch branch;

      @Column(nullable = false)
      private LocalDate startDate;

      @Column(nullable = false)
      private LocalDate endDate;

      @Column(nullable = false)
      private BigDecimal price;

      @Column(nullable = false)
      private BigDecimal discount = BigDecimal.ZERO;

      @Column(nullable = false)
      private BigDecimal finalAmount;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private MembershipStatus status = MembershipStatus.ACTIVE;

      public Membership() {
      }

      public Membership(Long id, Member member, MembershipPlan plan, Branch branch, LocalDate startDate, LocalDate endDate, BigDecimal price, BigDecimal discount, BigDecimal finalAmount, MembershipStatus status) {
            this.id = id;
            this.member = member;
            this.plan = plan;
            this.branch = branch;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
            this.discount = discount != null ? discount : BigDecimal.ZERO;
            this.finalAmount = finalAmount;
            this.status = status != null ? status : MembershipStatus.ACTIVE;
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

      public MembershipPlan getPlan() {
            return plan;
      }

      public void setPlan(MembershipPlan plan) {
            this.plan = plan;
      }

      public Branch getBranch() {
            return branch;
      }

      public void setBranch(Branch branch) {
            this.branch = branch;
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

      public BigDecimal getPrice() {
            return price;
      }

      public void setPrice(BigDecimal price) {
            this.price = price;
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

      public MembershipStatus getStatus() {
            return status;
      }

      public void setStatus(MembershipStatus status) {
            this.status = status;
      }
}
