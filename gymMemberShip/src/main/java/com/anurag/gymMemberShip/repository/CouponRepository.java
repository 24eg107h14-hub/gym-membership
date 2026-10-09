package com.anurag.gymMemberShip.repository;

import com.anurag.gymMemberShip.entity.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

      Optional<Coupon> findByCodeIgnoreCase(String code);

      boolean existsByCodeIgnoreCase(String code);

      List<Coupon> findByActive(boolean active);
}
