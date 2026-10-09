package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Branch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

      List<Branch> findByActive(boolean active);

      boolean existsByName(String name);
}
