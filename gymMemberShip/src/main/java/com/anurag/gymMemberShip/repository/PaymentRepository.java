package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Payment;
import com.anurag.gymMemberShip.enums.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

      List<Payment> findByMemberIdOrderByPaymentDateDesc(Long memberId);

      List<Payment> findByMemberUserIdOrderByPaymentDateDesc(Long userId);

      List<Payment> findByPaymentStatus(PaymentStatus status);

      boolean existsByTransactionId(String transactionId);
}
