package com.anurag.gymMemberShip.entity;

import com.anurag.gymMemberShip.enums.PaymentMethod;
import com.anurag.gymMemberShip.enums.PaymentStatus;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "member_id", nullable = false)
      private Member member;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "membership_id")
      private Membership membership;

      @Column(nullable = false)
      private BigDecimal amount;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private PaymentMethod paymentMethod = PaymentMethod.UPI;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false)
      private PaymentStatus paymentStatus = PaymentStatus.PAID;

      @Column(nullable = false, unique = true)
      private String transactionId;

      @Column(nullable = false)
      private LocalDateTime paymentDate = LocalDateTime.now();

      public Payment() {
      }

      public Payment(Long id, Member member, Membership membership, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String transactionId, LocalDateTime paymentDate) {
            this.id = id;
            this.member = member;
            this.membership = membership;
            this.amount = amount;
            this.paymentMethod = paymentMethod != null ? paymentMethod : PaymentMethod.UPI;
            this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PAID;
            this.transactionId = transactionId;
            this.paymentDate = paymentDate != null ? paymentDate : LocalDateTime.now();
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

      public Membership getMembership() {
            return membership;
      }

      public void setMembership(Membership membership) {
            this.membership = membership;
      }

      public BigDecimal getAmount() {
            return amount;
      }

      public void setAmount(BigDecimal amount) {
            this.amount = amount;
      }

      public PaymentMethod getPaymentMethod() {
            return paymentMethod;
      }

      public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
      }

      public PaymentStatus getPaymentStatus() {
            return paymentStatus;
      }

      public void setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
      }

      public String getTransactionId() {
            return transactionId;
      }

      public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
      }

      public LocalDateTime getPaymentDate() {
            return paymentDate;
      }

      public void setPaymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
      }
}
