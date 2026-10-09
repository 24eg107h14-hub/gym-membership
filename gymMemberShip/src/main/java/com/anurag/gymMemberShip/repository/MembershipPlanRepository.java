package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.MembershipPlan;
import com.anurag.gymMemberShip.enums.PlanStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

      List<MembershipPlan> findByStatus(PlanStatus status);
}
