package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

      Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

      List<Invoice> findByMemberIdOrderByInvoiceDateDesc(Long memberId);

      List<Invoice> findByMemberUserIdOrderByInvoiceDateDesc(Long userId);

      boolean existsByInvoiceNumber(String invoiceNumber);
}
