package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.PaymentDto;
import com.anurag.gymMemberShip.dto.PaymentResponseDto;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.entity.Payment;
import com.anurag.gymMemberShip.enums.PaymentMethod;
import com.anurag.gymMemberShip.enums.PaymentStatus;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.MembershipRepository;
import com.anurag.gymMemberShip.repository.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

      @Autowired
      private PaymentRepository paymentRepository;

      @Autowired
      private MemberRepository memberRepository;

      @Autowired
      private MembershipRepository membershipRepository;

      public List<PaymentResponseDto> getAll() {
            return paymentRepository.findAll().stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      public PaymentResponseDto getById(Long id) {
            Payment payment = paymentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
            return mapToResponseDto(payment);
      }

      public List<PaymentResponseDto> getMyPayments(String userEmail) {
            Member member = memberRepository.findByEmail(userEmail).orElse(null);
            if (member == null) {
                  return List.of();
            }
            return paymentRepository.findByMemberIdOrderByPaymentDateDesc(member.getId()).stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      public Payment createPaymentRecord(Member member, Membership membership, BigDecimal amount, PaymentMethod method, PaymentStatus status) {
            Payment payment = new Payment();
            payment.setMember(member);
            payment.setMembership(membership);
            payment.setAmount(amount);
            payment.setPaymentMethod(method != null ? method : PaymentMethod.UPI);
            payment.setPaymentStatus(status != null ? status : PaymentStatus.PAID);
            payment.setTransactionId("TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            payment.setPaymentDate(LocalDateTime.now());

            return paymentRepository.save(payment);
      }

      public PaymentResponseDto update(Long id, PaymentDto dto) {
            Payment payment = paymentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

            if (dto.getPaymentMethod() != null) {
                  payment.setPaymentMethod(dto.getPaymentMethod());
            }
            if (dto.getPaymentStatus() != null) {
                  payment.setPaymentStatus(dto.getPaymentStatus());
            }
            if (dto.getAmount() != null) {
                  payment.setAmount(dto.getAmount());
            }

            Payment updated = paymentRepository.save(payment);
            return mapToResponseDto(updated);
      }

      public void delete(Long id) {
            if (!paymentRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Payment not found with id: " + id);
            }
            paymentRepository.deleteById(id);
      }

      public PaymentResponseDto mapToResponseDto(Payment payment) {
            String planName = null;
            if (payment.getMembership() != null && payment.getMembership().getPlan() != null) {
                  planName = payment.getMembership().getPlan().getName();
            }

            return new PaymentResponseDto(
                        payment.getId(),
                        payment.getMember() != null ? payment.getMember().getId() : null,
                        payment.getMember() != null ? payment.getMember().getFullName() : null,
                        payment.getMember() != null ? payment.getMember().getEmail() : null,
                        payment.getMembership() != null ? payment.getMembership().getId() : null,
                        planName,
                        payment.getAmount(),
                        payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : "UPI",
                        payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : "PAID",
                        payment.getTransactionId(),
                        payment.getPaymentDate()
            );
      }
}
