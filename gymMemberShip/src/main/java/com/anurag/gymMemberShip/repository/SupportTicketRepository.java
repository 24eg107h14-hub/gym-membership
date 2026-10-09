package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.SupportTicket;
import com.anurag.gymMemberShip.enums.TicketCategory;
import com.anurag.gymMemberShip.enums.TicketStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

      List<SupportTicket> findByMemberIdOrderByCreatedAtDesc(Long memberId);

      List<SupportTicket> findByMemberUserIdOrderByCreatedAtDesc(Long userId);

      List<SupportTicket> findByStatus(TicketStatus status);

      List<SupportTicket> findByCategory(TicketCategory category);

      List<SupportTicket> findByStatusAndCategory(TicketStatus status, TicketCategory category);

      long countByStatus(TicketStatus status);
}
