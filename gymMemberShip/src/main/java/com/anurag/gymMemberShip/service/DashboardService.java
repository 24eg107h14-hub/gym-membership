package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.DashboardStatsDto;
import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.entity.Payment;
import com.anurag.gymMemberShip.enums.MembershipStatus;
import com.anurag.gymMemberShip.enums.PaymentStatus;
import com.anurag.gymMemberShip.enums.PlanStatus;
import com.anurag.gymMemberShip.enums.TicketStatus;
import com.anurag.gymMemberShip.repository.BranchRepository;
import com.anurag.gymMemberShip.repository.CouponRepository;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.MembershipPlanRepository;
import com.anurag.gymMemberShip.repository.MembershipRepository;
import com.anurag.gymMemberShip.repository.PaymentRepository;
import com.anurag.gymMemberShip.repository.SupportTicketRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

      @Autowired
      private MemberRepository memberRepository;

      @Autowired
      private MembershipRepository membershipRepository;

      @Autowired
      private MembershipPlanRepository planRepository;

      @Autowired
      private PaymentRepository paymentRepository;

      @Autowired
      private CouponRepository couponRepository;

      @Autowired
      private BranchRepository branchRepository;

      @Autowired
      private SupportTicketRepository ticketRepository;

      public DashboardStatsDto getStats() {
            long totalMembers = memberRepository.count();
            List<Membership> memberships = membershipRepository.findAll();

            long active = 0;
            long expiringSoon = 0;
            long expired = 0;
            LocalDate today = LocalDate.now();
            LocalDate sevenDaysLater = today.plusDays(7);

            for (Membership m : memberships) {
                  if (m.getStatus() == MembershipStatus.CANCELLED || m.getStatus() == MembershipStatus.FROZEN) {
                        continue;
                  }
                  if (m.getEndDate() == null || m.getEndDate().isBefore(today)) {
                        expired++;
                  } else if (!m.getEndDate().isAfter(sevenDaysLater)) {
                        expiringSoon++;
                        active++;
                  } else {
                        active++;
                  }
            }

            long totalPlans = planRepository.count();
            long activePlans = planRepository.findByStatus(PlanStatus.ACTIVE).size();

            List<Payment> payments = paymentRepository.findAll();
            long totalPayments = payments.size();
            long paidPayments = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;

            for (Payment p : payments) {
                  if (p.getPaymentStatus() == PaymentStatus.PAID) {
                        paidPayments++;
                        if (p.getAmount() != null) {
                              totalRevenue = totalRevenue.add(p.getAmount());
                        }
                  }
            }

            long totalCoupons = couponRepository.count();
            long activeCoupons = couponRepository.findByActive(true).size();

            long totalBranches = branchRepository.count();
            long activeBranches = branchRepository.findByActive(true).size();

            long totalTickets = ticketRepository.count();
            long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);

            return new DashboardStatsDto(
                        totalMembers,
                        active,
                        expiringSoon,
                        expired,
                        totalPlans,
                        activePlans,
                        totalPayments,
                        paidPayments,
                        totalRevenue,
                        totalCoupons,
                        activeCoupons,
                        totalBranches,
                        activeBranches,
                        totalTickets,
                        openTickets
            );
      }
}
