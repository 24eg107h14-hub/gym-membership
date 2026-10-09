package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.SupportDashboardStatsDto;
import com.anurag.gymMemberShip.dto.SupportTicketDto;
import com.anurag.gymMemberShip.dto.TicketResponseDto;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.SupportTicket;
import com.anurag.gymMemberShip.enums.TicketCategory;
import com.anurag.gymMemberShip.enums.TicketStatus;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.SupportTicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupportTicketService {

      @Autowired
      private SupportTicketRepository ticketRepository;

      @Autowired
      private MemberRepository memberRepository;

      @Transactional
      public TicketResponseDto create(SupportTicketDto dto, String userEmail) {
            Member member = memberRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("Member profile not found for user: " + userEmail));

            SupportTicket ticket = new SupportTicket();
            ticket.setMember(member);
            ticket.setSubject(dto.getSubject());
            ticket.setDescription(dto.getDescription());
            ticket.setCategory(dto.getCategory() != null ? dto.getCategory() : TicketCategory.OTHER);
            ticket.setStatus(TicketStatus.OPEN);
            ticket.setCreatedAt(LocalDateTime.now());

            SupportTicket saved = ticketRepository.save(ticket);
            return mapToResponseDto(saved);
      }

      public List<TicketResponseDto> getMyTickets(String userEmail) {
            Member member = memberRepository.findByEmail(userEmail).orElse(null);
            if (member == null) {
                  return List.of();
            }
            return ticketRepository.findByMemberIdOrderByCreatedAtDesc(member.getId()).stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      public TicketResponseDto getById(Long id, String userEmail, boolean isAdminOrSupport) {
            SupportTicket ticket = ticketRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with id: " + id));

            if (!isAdminOrSupport) {
                  if (ticket.getMember() == null || ticket.getMember().getEmail() == null || !ticket.getMember().getEmail().equalsIgnoreCase(userEmail)) {
                        throw new IllegalArgumentException("You are not authorized to view this support ticket");
                  }
            }

            return mapToResponseDto(ticket);
      }

      public List<TicketResponseDto> getAll(TicketStatus status, TicketCategory category) {
            List<SupportTicket> list;
            if (status != null && category != null) {
                  list = ticketRepository.findByStatusAndCategory(status, category);
            } else if (status != null) {
                  list = ticketRepository.findByStatus(status);
            } else if (category != null) {
                  list = ticketRepository.findByCategory(category);
            } else {
                  list = ticketRepository.findAll();
            }

            return list.stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList());
      }

      @Transactional
      public TicketResponseDto updateStatus(Long id, TicketStatus status) {
            SupportTicket ticket = ticketRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with id: " + id));

            ticket.setStatus(status);
            ticket.setUpdatedAt(LocalDateTime.now());

            SupportTicket updated = ticketRepository.save(ticket);
            return mapToResponseDto(updated);
      }

      @Transactional
      public TicketResponseDto respond(Long id, String responseText) {
            SupportTicket ticket = ticketRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with id: " + id));

            ticket.setResponse(responseText);
            ticket.setUpdatedAt(LocalDateTime.now());
            if (ticket.getStatus() == TicketStatus.OPEN) {
                  ticket.setStatus(TicketStatus.IN_PROGRESS);
            }

            SupportTicket updated = ticketRepository.save(ticket);
            return mapToResponseDto(updated);
      }

      public void delete(Long id) {
            if (!ticketRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Support ticket not found with id: " + id);
            }
            ticketRepository.deleteById(id);
      }

      public SupportDashboardStatsDto getSupportDashboardStats() {
            long total = ticketRepository.count();
            long open = ticketRepository.countByStatus(TicketStatus.OPEN);
            long inProgress = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
            long resolved = ticketRepository.countByStatus(TicketStatus.RESOLVED);
            long closed = ticketRepository.countByStatus(TicketStatus.CLOSED);

            return new SupportDashboardStatsDto(total, open, inProgress, resolved, closed);
      }

      public TicketResponseDto mapToResponseDto(SupportTicket ticket) {
            return new TicketResponseDto(
                        ticket.getId(),
                        ticket.getMember() != null ? ticket.getMember().getId() : null,
                        ticket.getMember() != null ? ticket.getMember().getFullName() : null,
                        ticket.getMember() != null ? ticket.getMember().getEmail() : null,
                        ticket.getSubject(),
                        ticket.getDescription(),
                        ticket.getCategory() != null ? ticket.getCategory().name() : "OTHER",
                        ticket.getStatus() != null ? ticket.getStatus().name() : "OPEN",
                        ticket.getCreatedAt(),
                        ticket.getUpdatedAt(),
                        ticket.getResponse()
            );
      }
}
