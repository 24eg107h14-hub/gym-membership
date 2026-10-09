package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Membership;
import com.anurag.gymMemberShip.enums.MembershipStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

      List<Membership> findByMemberId(Long memberId);

      Optional<Membership> findFirstByMemberIdOrderByEndDateDesc(Long memberId);

      Optional<Membership> findFirstByMemberUserIdOrderByEndDateDesc(Long userId);

      List<Membership> findByStatus(MembershipStatus status);

      boolean existsByPlanId(Long planId);
}
