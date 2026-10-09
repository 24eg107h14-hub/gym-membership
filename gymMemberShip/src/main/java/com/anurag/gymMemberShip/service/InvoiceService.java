package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.InvoiceResponseDto;
import com.anurag.gymMemberShip.entity.Invoice;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.entity.Payment;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.InvoiceRepository;
import com.anurag.gymMemberShip.repository.MemberRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

      @Autowired
      private InvoiceRepository invoiceRepository;

      @Autowired
      private MemberRepository memberRepository;

      private static final AtomicLong INVOICE_SEQ = new AtomicLong(1000);

      public List<InvoiceResponseDto> getAll() {
            return invoiceRepository.findAll().stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      public InvoiceResponseDto getById(Long id, String userEmail, boolean isAdmin) {
            Invoice invoice = invoiceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

            if (!isAdmin) {
                  if (invoice.getMember() == null || invoice.getMember().getEmail() == null || !invoice.getMember().getEmail().equalsIgnoreCase(userEmail)) {
                        throw new IllegalArgumentException("You are not authorized to view this invoice");
                  }
            }

            return mapToResponseDto(invoice);
      }

      public List<InvoiceResponseDto> getMyInvoices(String userEmail) {
            Member member = memberRepository.findByEmail(userEmail).orElse(null);
            if (member == null) {
                  return List.of();
            }
            return invoiceRepository.findByMemberIdOrderByInvoiceDateDesc(member.getId()).stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      public Invoice generateInvoice(Member member, Membership membership, Payment payment, BigDecimal amount, BigDecimal discount, BigDecimal finalAmount) {
            String invoiceNumber = "INV-" + Year.now().getValue() + "-" + (INVOICE_SEQ.incrementAndGet());

            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setMember(member);
            invoice.setMembership(membership);
            invoice.setPayment(payment);
            invoice.setAmount(amount);
            invoice.setDiscount(discount != null ? discount : BigDecimal.ZERO);
            invoice.setFinalAmount(finalAmount);
            invoice.setInvoiceDate(LocalDateTime.now());

            return invoiceRepository.save(invoice);
      }

      public void delete(Long id) {
            if (!invoiceRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Invoice not found with id: " + id);
            }
            invoiceRepository.deleteById(id);
      }

      public InvoiceResponseDto mapToResponseDto(Invoice invoice) {
            String planName = null;
            String branchName = null;
            if (invoice.getMembership() != null) {
                  if (invoice.getMembership().getPlan() != null) {
                        planName = invoice.getMembership().getPlan().getName();
                  }
                  if (invoice.getMembership().getBranch() != null) {
                        branchName = invoice.getMembership().getBranch().getName();
                  }
            }

            String paymentMethod = null;
            String transactionId = null;
            if (invoice.getPayment() != null) {
                  paymentMethod = invoice.getPayment().getPaymentMethod() != null ? invoice.getPayment().getPaymentMethod().name() : null;
                  transactionId = invoice.getPayment().getTransactionId();
            }

            return new InvoiceResponseDto(
                        invoice.getId(),
                        invoice.getInvoiceNumber(),
                        invoice.getMember() != null ? invoice.getMember().getId() : null,
                        invoice.getMember() != null ? invoice.getMember().getFullName() : null,
                        invoice.getMember() != null ? invoice.getMember().getEmail() : null,
                        invoice.getMember() != null ? invoice.getMember().getPhone() : null,
                        invoice.getMember() != null ? invoice.getMember().getAddress() : null,
                        invoice.getMembership() != null ? invoice.getMembership().getId() : null,
                        planName,
                        branchName,
                        invoice.getPayment() != null ? invoice.getPayment().getId() : null,
                        paymentMethod,
                        transactionId,
                        invoice.getAmount(),
                        invoice.getDiscount(),
                        invoice.getFinalAmount(),
                        invoice.getInvoiceDate()
            );
      }
}
