package com.anurag.gymMemberShip.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(nullable = false, unique = true)
      private String invoiceNumber;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "member_id", nullable = false)
      private Member member;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "membership_id")
      private Membership membership;

      @OneToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "payment_id")
      private Payment payment;

      @Column(nullable = false)
      private BigDecimal amount;

      @Column(nullable = false)
      private BigDecimal discount = BigDecimal.ZERO;

      @Column(nullable = false)
      private BigDecimal finalAmount;

      @Column(nullable = false)
      private LocalDateTime invoiceDate = LocalDateTime.now();

      public Invoice() {
      }

      public Invoice(Long id, String invoiceNumber, Member member, Membership membership, Payment payment, BigDecimal amount, BigDecimal discount, BigDecimal finalAmount, LocalDateTime invoiceDate) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.member = member;
            this.membership = membership;
            this.payment = payment;
            this.amount = amount;
            this.discount = discount != null ? discount : BigDecimal.ZERO;
            this.finalAmount = finalAmount;
            this.invoiceDate = invoiceDate != null ? invoiceDate : LocalDateTime.now();
      }

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getInvoiceNumber() {
            return invoiceNumber;
      }

      public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
      }

      public Member getMember() {
            return member;
      }

      public void setMember(Member member) {
            this.member = member;
      }

      public Membership getMembership() {
            return membership;
      }

      public void setMembership(Membership membership) {
            this.membership = membership;
      }

      public Payment getPayment() {
            return payment;
      }

      public void setPayment(Payment payment) {
            this.payment = payment;
      }

      public BigDecimal getAmount() {
            return amount;
      }

      public void setAmount(BigDecimal amount) {
            this.amount = amount;
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

      public LocalDateTime getInvoiceDate() {
            return invoiceDate;
      }

      public void setInvoiceDate(LocalDateTime invoiceDate) {
            this.invoiceDate = invoiceDate;
      }
}
