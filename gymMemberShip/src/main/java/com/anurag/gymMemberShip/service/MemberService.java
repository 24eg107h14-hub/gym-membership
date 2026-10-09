package com.anurag.gymMemberShip.service;

import com.anurag.gymMemberShip.dto.MemberDto;
import com.anurag.gymMemberShip.dto.MemberResponseDto;
import com.anurag.gymMemberShip.dto.UserProfileDto;
import com.anurag.gymMemberShip.entity.Member;
import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.entity.MembershipPlan;
import com.anurag.gymMemberShip.entity.User;
import com.anurag.gymMemberShip.enums.MembershipStatus;
import com.anurag.gymMemberShip.enums.PlanStatus;
import com.anurag.gymMemberShip.enums.Role;
import com.anurag.gymMemberShip.exception.ResourceNotFoundException;
import com.anurag.gymMemberShip.repository.MemberRepository;
import com.anurag.gymMemberShip.repository.MembershipPlanRepository;
import com.anurag.gymMemberShip.repository.MembershipRepository;
import com.anurag.gymMemberShip.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

      @Autowired
      private MemberRepository memberRepository;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private MembershipRepository membershipRepository;

      @Autowired
      private MembershipPlanRepository planRepository;

      @Autowired
      private PasswordEncoder passwordEncoder;

      public List<MemberResponseDto> getAll(String search, String status) {
            List<Member> members;

            if (search != null && !search.trim().isEmpty()) {
                  String query = search.trim();
                  if (query.matches("\\d+")) {
                        Long searchId = Long.parseLong(query);
                        members = new ArrayList<>();
                        memberRepository.findById(searchId).ifPresent(members::add);
                        List<Member> textMatches = memberRepository.search(query);
                        for (Member m : textMatches) {
                              if (!members.contains(m)) {
                                    members.add(m);
                              }
                        }
                  } else {
                        members = memberRepository.search(query);
                  }
            } else {
                  members = memberRepository.findAll();
            }

            return members.stream()
                        .map(this::mapToResponseDto)
                        .filter(dto -> {
                              if (status == null || status.trim().isEmpty() || "ALL".equalsIgnoreCase(status)) {
                                    return true;
                              }
                              return dto.getStatus() != null && dto.getStatus().equalsIgnoreCase(status.trim());
                        })
                        .collect(Collectors.toList());
      }

      public MemberResponseDto getById(Long id) {
            Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
            return mapToResponseDto(member);
      }

      public UserProfileDto getProfileByEmail(String email) {
            Member member = memberRepository.findByEmail(email).orElse(null);
            if (member == null) {
                  User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
                  return new UserProfileDto(user.getId(), user.getName(), user.getEmail(), "", "", "", null, user.getRole().name());
            }
            return new UserProfileDto(
                        member.getId(),
                        member.getFullName(),
                        member.getEmail(),
                        member.getPhone(),
                        member.getGender(),
                        member.getAddress(),
                        member.getDob(),
                        member.getUser() != null ? member.getUser().getRole().name() : Role.MEMBER.name()
            );
      }

      @Transactional
      public UserProfileDto updateProfile(String email, UserProfileDto dto) {
            Member member = memberRepository.findByEmail(email).orElse(null);
            if (member == null) {
                  User user = userRepository.findByEmail(email)
                              .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
                  user.setName(dto.getName());
                  userRepository.save(user);

                  Member newMember = new Member();
                  newMember.setUser(user);
                  newMember.setFullName(dto.getName());
                  newMember.setEmail(dto.getEmail());
                  newMember.setPhone(dto.getPhone() != null ? dto.getPhone() : "N/A-" + System.currentTimeMillis());
                  newMember.setGender(dto.getGender());
                  newMember.setAddress(dto.getAddress());
                  newMember.setDob(dto.getDob());
                  member = memberRepository.save(newMember);
            } else {
                  if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
                        if (memberRepository.existsByPhoneAndIdNot(dto.getPhone().trim(), member.getId())) {
                              throw new IllegalArgumentException("Phone number already in use");
                        }
                        member.setPhone(dto.getPhone().trim());
                  }
                  member.setFullName(dto.getName());
                  member.setGender(dto.getGender());
                  member.setAddress(dto.getAddress());
                  member.setDob(dto.getDob());

                  if (member.getUser() != null) {
                        member.getUser().setName(dto.getName());
                        userRepository.save(member.getUser());
                  }

                  member = memberRepository.save(member);
            }

            return new UserProfileDto(
                        member.getId(),
                        member.getFullName(),
                        member.getEmail(),
                        member.getPhone(),
                        member.getGender(),
                        member.getAddress(),
                        member.getDob(),
                        member.getUser() != null ? member.getUser().getRole().name() : Role.MEMBER.name()
            );
      }

      @Transactional
      public void changePassword(String email, com.anurag.gymMemberShip.dto.ChangePasswordDto dto) {
            User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                  throw new IllegalArgumentException("Current password is incorrect");
            }

            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                  throw new IllegalArgumentException("New password and confirm password do not match");
            }

            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);
      }

      @Transactional
      public MemberResponseDto add(MemberDto dto) {
            if (memberRepository.existsByPhone(dto.getPhone())) {
                  throw new IllegalArgumentException("Phone number already exists");
            }
            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                  if (memberRepository.existsByEmail(dto.getEmail().trim())) {
                        throw new IllegalArgumentException("Email already exists");
                  }
            }

            String email = (dto.getEmail() != null && !dto.getEmail().trim().isEmpty())
                        ? dto.getEmail().trim()
                        : "member" + System.currentTimeMillis() + "@gym.com";

            // Create User account if not exists
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                  user = new User();
                  user.setName(dto.getFullName());
                  user.setEmail(email);
                  user.setPassword(passwordEncoder.encode("gym123"));
                  user.setRole(Role.MEMBER);
                  user = userRepository.save(user);
            }

            Member member = new Member();
            member.setUser(user);
            member.setFullName(dto.getFullName());
            member.setPhone(dto.getPhone());
            member.setEmail(email);
            member.setGender(dto.getGender());
            member.setAddress(dto.getAddress());
            member.setDob(dto.getDob());

            Member savedMember = memberRepository.save(member);

            // If a plan is selected, create active Membership
            if (dto.getPlanId() != null) {
                  MembershipPlan plan = planRepository.findById(dto.getPlanId())
                              .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + dto.getPlanId()));

                  LocalDate joinDate = dto.getJoinDate() != null ? dto.getJoinDate() : LocalDate.now();
                  int duration = (plan.getDurationMonths() != null) ? plan.getDurationMonths() : 1;
                  LocalDate expiryDate = joinDate.plusMonths(duration);

                  Membership membership = new Membership();
                  membership.setMember(savedMember);
                  membership.setPlan(plan);
                  membership.setStartDate(joinDate);
                  membership.setEndDate(expiryDate);
                  membership.setPrice(plan.getPrice());
                  membership.setDiscount(BigDecimal.ZERO);
                  membership.setFinalAmount(plan.getPrice());
                  membership.setStatus(MembershipStatus.ACTIVE);

                  membershipRepository.save(membership);
            }

            return mapToResponseDto(savedMember);
      }

      @Transactional
      public MemberResponseDto update(Long id, MemberDto dto) {
            Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

            if (memberRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
                  throw new IllegalArgumentException("Phone number already in use by another member");
            }
            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                  if (memberRepository.existsByEmailAndIdNot(dto.getEmail().trim(), id)) {
                        throw new IllegalArgumentException("Email already in use by another member");
                  }
            }

            member.setFullName(dto.getFullName());
            member.setPhone(dto.getPhone());
            member.setEmail(dto.getEmail());
            member.setGender(dto.getGender());
            member.setAddress(dto.getAddress());
            member.setDob(dto.getDob());

            if (member.getUser() != null) {
                  member.getUser().setName(dto.getFullName());
                  if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                        member.getUser().setEmail(dto.getEmail().trim());
                  }
                  userRepository.save(member.getUser());
            }

            // If plan is updated
            if (dto.getPlanId() != null) {
                  MembershipPlan plan = planRepository.findById(dto.getPlanId())
                              .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + dto.getPlanId()));

                  Membership latest = membershipRepository.findFirstByMemberIdOrderByEndDateDesc(member.getId()).orElse(null);
                  LocalDate joinDate = dto.getJoinDate() != null ? dto.getJoinDate() : LocalDate.now();
                  int duration = (plan.getDurationMonths() != null) ? plan.getDurationMonths() : 1;

                  if (latest == null) {
                        Membership m = new Membership();
                        m.setMember(member);
                        m.setPlan(plan);
                        m.setStartDate(joinDate);
                        m.setEndDate(joinDate.plusMonths(duration));
                        m.setPrice(plan.getPrice());
                        m.setDiscount(BigDecimal.ZERO);
                        m.setFinalAmount(plan.getPrice());
                        m.setStatus(MembershipStatus.ACTIVE);
                        membershipRepository.save(m);
                  } else {
                        latest.setPlan(plan);
                        latest.setStartDate(joinDate);
                        latest.setEndDate(joinDate.plusMonths(duration));
                        latest.setPrice(plan.getPrice());
                        latest.setFinalAmount(plan.getPrice());
                        latest.setStatus(MembershipStatus.ACTIVE);
                        membershipRepository.save(latest);
                  }
            }

            Member updated = memberRepository.save(member);
            return mapToResponseDto(updated);
      }

      @Transactional
      public void delete(Long id) {
            Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

            List<Membership> memberships = membershipRepository.findByMemberId(id);
            membershipRepository.deleteAll(memberships);

            User user = member.getUser();
            memberRepository.delete(member);
            if (user != null && user.getRole() == Role.MEMBER) {
                  userRepository.delete(user);
            }
      }

      @Transactional
      public MemberResponseDto renew(Long id, Long planId) {
            Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

            Membership latest = membershipRepository.findFirstByMemberIdOrderByEndDateDesc(member.getId()).orElse(null);
            MembershipPlan plan;
            if (planId != null) {
                  plan = planRepository.findById(planId)
                              .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + planId));
            } else if (latest != null) {
                  plan = latest.getPlan();
            } else {
                  throw new IllegalArgumentException("No existing plan found to renew; specify a planId");
            }

            LocalDate today = LocalDate.now();
            LocalDate baseDate = (latest != null && latest.getEndDate() != null && !latest.getEndDate().isBefore(today) && latest.getStatus() == MembershipStatus.ACTIVE)
                        ? latest.getEndDate()
                        : today;

            int duration = (plan.getDurationMonths() != null) ? plan.getDurationMonths() : 1;
            LocalDate newExpiry = baseDate.plusMonths(duration);

            if (latest == null) {
                  latest = new Membership();
                  latest.setMember(member);
                  latest.setStartDate(today);
            }
            latest.setPlan(plan);
            latest.setEndDate(newExpiry);
            latest.setPrice(plan.getPrice());
            latest.setDiscount(BigDecimal.ZERO);
            latest.setFinalAmount(plan.getPrice());
            latest.setStatus(MembershipStatus.ACTIVE);

            membershipRepository.save(latest);
            return mapToResponseDto(member);
      }

      public MemberResponseDto mapToResponseDto(Member member) {
            Membership latest = membershipRepository.findFirstByMemberIdOrderByEndDateDesc(member.getId()).orElse(null);

            Long planId = null;
            String planName = null;
            LocalDate joinDate = null;
            LocalDate expiryDate = null;
            String statusStr = "NONE";
            long daysRemaining = 0;

            if (latest != null) {
                  planId = latest.getPlan() != null ? latest.getPlan().getId() : null;
                  planName = latest.getPlan() != null ? latest.getPlan().getName() : null;
                  joinDate = latest.getStartDate();
                  expiryDate = latest.getEndDate();
                  if (latest.getStatus() == MembershipStatus.ACTIVE && expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
                        statusStr = MembershipStatus.EXPIRED.name();
                  } else {
                        statusStr = latest.getStatus() != null ? latest.getStatus().name() : "ACTIVE";
                  }
                  if (expiryDate != null) {
                        daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
                  }
            }

            return new MemberResponseDto(
                        member.getId(),
                        member.getUser() != null ? member.getUser().getId() : null,
                        member.getFullName(),
                        member.getPhone(),
                        member.getEmail(),
                        member.getGender(),
                        member.getAddress(),
                        member.getDob(),
                        planId,
                        planName,
                        joinDate,
                        expiryDate,
                        statusStr,
                        daysRemaining
            );
      }
}
