package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.CouponValidateDto;
import com.anurag.gymMemberShip.dto.MembershipPurchaseDto;
import com.anurag.gymMemberShip.dto.MembershipResponseDto;
import com.anurag.gymMemberShip.entity.Branch;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.entity.MembershipPlan;
import com.anurag.gymMemberShip.entity.Payment;
import com.anurag.gymMemberShip.enums.MembershipStatus;
import com.anurag.gymMemberShip.enums.PaymentMethod;
import com.anurag.gymMemberShip.enums.PaymentStatus;
import com.anurag.gymMemberShip.enums.PlanStatus;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.BranchRepository;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.MembershipPlanRepository;
import com.anurag.gymMemberShip.repository.MembershipRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipService {

      @Autowired
      private MembershipRepository membershipRepository;

      @Autowired
      private MemberRepository memberRepository;

      @Autowired
      private MembershipPlanRepository planRepository;

      @Autowired
      private BranchRepository branchRepository;

      @Autowired
      private CouponService couponService;

      @Autowired
      private PaymentService paymentService;

      @Autowired
      private InvoiceService invoiceService;

      @Transactional
      public MembershipResponseDto purchase(MembershipPurchaseDto dto, String userEmail) {
            Member member = memberRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("Member profile not found for user: " + userEmail));

            MembershipPlan plan = planRepository.findById(dto.getPlanId())
                        .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + dto.getPlanId()));

            if (plan.getStatus() != PlanStatus.ACTIVE) {
                  throw new IllegalArgumentException("Cannot purchase an inactive membership plan");
            }

            Branch branch = null;
            if (dto.getBranchId() != null) {
                  branch = branchRepository.findById(dto.getBranchId())
                              .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + dto.getBranchId()));
            }

            BigDecimal price = plan.getPrice() != null ? plan.getPrice() : BigDecimal.ZERO;
            BigDecimal discount = BigDecimal.ZERO;
            BigDecimal finalAmount = price;

            // Apply coupon if supplied
            if (dto.getCouponCode() != null && !dto.getCouponCode().trim().isEmpty()) {
                  CouponValidateDto.Response valResp = couponService.validate(
                              new CouponValidateDto.Request(dto.getCouponCode().trim(), price)
                  );
                  if (!valResp.isValid()) {
                        throw new IllegalArgumentException(valResp.getMessage());
                  }
                  discount = valResp.getDiscountAmount();
                  finalAmount = valResp.getFinalAmount();
            }

            LocalDate startDate = dto.getStartDate() != null && !dto.getStartDate().isBefore(LocalDate.now())
                        ? dto.getStartDate()
                        : LocalDate.now();

            int durationMonths = plan.getDurationMonths() != null ? plan.getDurationMonths() : 1;
            LocalDate endDate = startDate.plusMonths(durationMonths);

            Membership membership = new Membership();
            membership.setMember(member);
            membership.setPlan(plan);
            membership.setBranch(branch);
            membership.setStartDate(startDate);
            membership.setEndDate(endDate);
            membership.setPrice(price);
            membership.setDiscount(discount);
            membership.setFinalAmount(finalAmount);
            membership.setStatus(MembershipStatus.ACTIVE);

            Membership saved = membershipRepository.save(membership);

            // Create Payment Record (Simulated Payment)
            PaymentMethod method = dto.getPaymentMethod() != null ? dto.getPaymentMethod() : PaymentMethod.UPI;
            Payment payment = paymentService.createPaymentRecord(member, saved, finalAmount, method, PaymentStatus.PAID);

            // Generate Invoice
            invoiceService.generateInvoice(member, saved, payment, price, discount, finalAmount);

            return mapToResponseDto(saved);
      }

      public MembershipResponseDto getCurrentMembership(String userEmail) {
            Member member = memberRepository.findByEmail(userEmail).orElse(null);
            if (member == null) {
                  return null;
            }

            Membership membership = membershipRepository.findFirstByMemberIdOrderByEndDateDesc(member.getId()).orElse(null);
            if (membership == null) {
                  return null;
            }

            // Sync expired status if needed
            if (membership.getStatus() == MembershipStatus.ACTIVE && membership.getEndDate().isBefore(LocalDate.now())) {
                  membership.setStatus(MembershipStatus.EXPIRED);
                  membership = membershipRepository.save(membership);
            }

            return mapToResponseDto(membership);
      }

      @Transactional
      public MembershipResponseDto cancel(Long membershipId, String userEmail, boolean isAdmin) {
            Membership membership = membershipRepository.findById(membershipId)
                        .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + membershipId));

            verifyOwnership(membership, userEmail, isAdmin);

            if (membership.getStatus() == MembershipStatus.CANCELLED) {
                  throw new IllegalArgumentException("Membership is already cancelled");
            }

            membership.setStatus(MembershipStatus.CANCELLED);
            Membership updated = membershipRepository.save(membership);
            return mapToResponseDto(updated);
      }

      @Transactional
      public MembershipResponseDto freeze(Long membershipId, String userEmail, boolean isAdmin) {
            Membership membership = membershipRepository.findById(membershipId)
                        .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + membershipId));

            verifyOwnership(membership, userEmail, isAdmin);

            if (membership.getStatus() != MembershipStatus.ACTIVE) {
                  throw new IllegalArgumentException("Only active memberships can be frozen");
            }

            membership.setStatus(MembershipStatus.FROZEN);
            Membership updated = membershipRepository.save(membership);
            return mapToResponseDto(updated);
      }

      @Transactional
      public MembershipResponseDto renew(Long membershipId, Long planId, String userEmail, boolean isAdmin) {
            Membership membership = membershipRepository.findById(membershipId)
                        .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + membershipId));

            verifyOwnership(membership, userEmail, isAdmin);

            MembershipPlan plan;
            if (planId != null) {
                  plan = planRepository.findById(planId)
                              .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));
            } else {
                  plan = membership.getPlan();
            }

            if (plan.getStatus() != PlanStatus.ACTIVE) {
                  throw new IllegalArgumentException("Cannot renew with an inactive plan");
            }

            LocalDate today = LocalDate.now();
            LocalDate baseDate = (membership.getEndDate() != null && !membership.getEndDate().isBefore(today) && membership.getStatus() == MembershipStatus.ACTIVE)
                        ? membership.getEndDate()
                        : today;

            int durationMonths = plan.getDurationMonths() != null ? plan.getDurationMonths() : 1;
            LocalDate newExpiry = baseDate.plusMonths(durationMonths);

            membership.setPlan(plan);
            membership.setEndDate(newExpiry);
            membership.setPrice(plan.getPrice());
            membership.setDiscount(BigDecimal.ZERO);
            membership.setFinalAmount(plan.getPrice());
            membership.setStatus(MembershipStatus.ACTIVE);

            Membership renewed = membershipRepository.save(membership);

            // Record renewal payment and invoice
            Payment payment = paymentService.createPaymentRecord(membership.getMember(), renewed, plan.getPrice(), PaymentMethod.UPI, PaymentStatus.PAID);
            invoiceService.generateInvoice(membership.getMember(), renewed, payment, plan.getPrice(), BigDecimal.ZERO, plan.getPrice());

            return mapToResponseDto(renewed);
      }

      @Transactional
      public MembershipResponseDto upgrade(Long membershipId, Long newPlanId, String userEmail, boolean isAdmin) {
            Membership membership = membershipRepository.findById(membershipId)
                        .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + membershipId));

            verifyOwnership(membership, userEmail, isAdmin);

            MembershipPlan newPlan = planRepository.findById(newPlanId)
                        .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + newPlanId));

            if (newPlan.getStatus() != PlanStatus.ACTIVE) {
                  throw new IllegalArgumentException("Cannot upgrade to an inactive plan");
            }

            int durationMonths = newPlan.getDurationMonths() != null ? newPlan.getDurationMonths() : 1;
            LocalDate startDate = LocalDate.now();
            LocalDate newEndDate = startDate.plusMonths(durationMonths);

            membership.setPlan(newPlan);
            membership.setStartDate(startDate);
            membership.setEndDate(newEndDate);
            membership.setPrice(newPlan.getPrice());
            membership.setDiscount(BigDecimal.ZERO);
            membership.setFinalAmount(newPlan.getPrice());
            membership.setStatus(MembershipStatus.ACTIVE);

            Membership upgraded = membershipRepository.save(membership);

            // Record upgrade payment and invoice
            Payment payment = paymentService.createPaymentRecord(membership.getMember(), upgraded, newPlan.getPrice(), PaymentMethod.UPI, PaymentStatus.PAID);
            invoiceService.generateInvoice(membership.getMember(), upgraded, payment, newPlan.getPrice(), BigDecimal.ZERO, newPlan.getPrice());

            return mapToResponseDto(upgraded);
      }

      public List<MembershipResponseDto> getAllMemberships() {
            return membershipRepository.findAll().stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      private void verifyOwnership(Membership membership, String userEmail, boolean isAdmin) {
            if (isAdmin) {
                  return;
            }
            if (membership.getMember() == null || membership.getMember().getEmail() == null || !membership.getMember().getEmail().equalsIgnoreCase(userEmail)) {
                  throw new IllegalArgumentException("You are not authorized to modify this membership");
            }
      }

      public MembershipResponseDto mapToResponseDto(Membership membership) {
            long daysRemaining = 0;
            if (membership.getEndDate() != null) {
                  daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), membership.getEndDate());
            }

            Long branchId = null;
            String branchName = null;
            if (membership.getBranch() != null) {
                  branchId = membership.getBranch().getId();
                  branchName = membership.getBranch().getName();
            }

            return new MembershipResponseDto(
                        membership.getId(),
                        membership.getMember() != null ? membership.getMember().getId() : null,
                        membership.getMember() != null ? membership.getMember().getFullName() : null,
                        membership.getMember() != null ? membership.getMember().getEmail() : null,
                        membership.getPlan() != null ? membership.getPlan().getId() : null,
                        membership.getPlan() != null ? membership.getPlan().getName() : null,
                        membership.getPlan() != null ? membership.getPlan().getDurationMonths() : null,
                        branchId,
                        branchName,
                        membership.getStartDate(),
                        membership.getEndDate(),
                        membership.getPrice(),
                        membership.getDiscount(),
                        membership.getFinalAmount(),
                        membership.getStatus() != null ? membership.getStatus().name() : "ACTIVE",
                        daysRemaining
            );
      }
}
