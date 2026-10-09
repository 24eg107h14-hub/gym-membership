package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.PlanDto;
import com.anurag.gymMemberShip.entity.MembershipPlan;
import com.anurag.gymMemberShip.enums.PlanStatus;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.MembershipPlanRepository;
import com.anurag.gymMemberShip.repository.MembershipRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {

      @Autowired
      private MembershipPlanRepository planRepository;

      @Autowired
      private MembershipRepository membershipRepository;

      public List<PlanDto> getAll() {
            return planRepository.findAll().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public List<PlanDto> getActivePlans() {
            return planRepository.findByStatus(PlanStatus.ACTIVE).stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());
      }

      public PlanDto getById(Long id) {
            MembershipPlan plan = planRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
            return mapToDto(plan);
      }

      public PlanDto add(PlanDto dto) {
            MembershipPlan plan = new MembershipPlan();
            plan.setName(dto.getName());
            plan.setDurationMonths(dto.getDurationMonths());
            plan.setPrice(dto.getPrice());
            plan.setDescription(dto.getDescription());
            plan.setBenefits(dto.getBenefits());
            plan.setStatus(dto.getStatus() != null ? dto.getStatus() : PlanStatus.ACTIVE);

            MembershipPlan saved = planRepository.save(plan);
            return mapToDto(saved);
      }

      public PlanDto update(Long id, PlanDto dto) {
            MembershipPlan plan = planRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));

            plan.setName(dto.getName());
            plan.setDurationMonths(dto.getDurationMonths());
            plan.setPrice(dto.getPrice());
            plan.setDescription(dto.getDescription());
            plan.setBenefits(dto.getBenefits());
            if (dto.getStatus() != null) {
                  plan.setStatus(dto.getStatus());
            }

            MembershipPlan updated = planRepository.save(plan);
            return mapToDto(updated);
      }

      public PlanDto toggleStatus(Long id) {
            MembershipPlan plan = planRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));

            plan.setStatus(plan.getStatus() == PlanStatus.ACTIVE ? PlanStatus.INACTIVE : PlanStatus.ACTIVE);
            MembershipPlan updated = planRepository.save(plan);
            return mapToDto(updated);
      }

      public void delete(Long id) {
            if (!planRepository.existsById(id)) {
                  throw new ResourceNotFoundException("Plan not found with id: " + id);
            }
            if (membershipRepository.existsByPlanId(id)) {
                  throw new IllegalArgumentException("Plan is in use by memberships and cannot be deleted");
            }
            planRepository.deleteById(id);
      }

      public PlanDto mapToDto(MembershipPlan plan) {
            return new PlanDto(
                        plan.getId(),
                        plan.getName(),
                        plan.getDurationMonths(),
                        plan.getPrice(),
                        plan.getDescription(),
                        plan.getBenefits(),
                        plan.getStatus()
            );
      }
}
